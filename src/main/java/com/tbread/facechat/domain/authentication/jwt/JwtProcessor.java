package com.tbread.facechat.domain.authentication.jwt;

import com.tbread.facechat.domain.authentication.jwt.entity.RefreshToken;
import com.tbread.facechat.domain.common.TokenPackage;
import com.tbread.facechat.domain.user.UserRepository;
import com.tbread.facechat.domain.user.entity.User;
import com.tbread.facechat.util.ExpiringHashMap;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtProcessor {

    @Getter
    public enum TimeUnit {
        SECOND(1000L),
        MINUTE(60 * 1000L),
        HOUR(60 * 60 * 1000L),
        DAY(24 * 60 * 60 * 1000L),
        WEEK(7 * 24 * 60 * 60 * 1000L);
        private final long value;

        private static final Map<String, TimeUnit> TimeUnitMatcher = Map.of(
                "second", TimeUnit.SECOND,
                "minute", TimeUnit.MINUTE,
                "hour", TimeUnit.HOUR,
                "day", TimeUnit.DAY,
                "week", TimeUnit.WEEK
        );

        TimeUnit(long value) {
            this.value = value;
        }

        public static long getTimeValue(String s) {
            return TimeUnitMatcher.get(s).getValue();
        }

    }

    public enum JwtType {
        ACCESS("Access-Token"), REFRESH("Refresh-Token");
        private String cookieName;

        JwtType(String cookieName) {
            this.cookieName = cookieName;
        }

        public String getCookieName() {
            return this.cookieName;
        }
    }

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    private final long ACCESS_TOKEN_VALID_TIME;
    private final long REFRESH_TOKEN_VALID_TIME;
    private final static ExpiringHashMap<String, Boolean> INVALIDATED_REFRESH_TOKEN = new ExpiringHashMap<>();

    private final String secretKey;

    @Autowired
    public JwtProcessor(@Value("${jwt.validate.time.access:#{null}}") String accessValidTime,
                        @Value("${jwt.validate.time.access.unit:#{null}}") String accessValidTimeUnit,
                        @Value("${jwt.validate.time.refresh:#{null}}") String refreshValidTime,
                        @Value("${jwt.validate.time.refresh.unit:#{null}}") String refreshValidTimeUnit,
                        @Value("${jwt.signing.secret:#{null}}") String rawSecretKey,
                        RefreshTokenRepository refreshTokenRepository,
                        UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;

        try {
            int parsedAccessValidTime = Integer.parseInt(accessValidTime);
            this.ACCESS_TOKEN_VALID_TIME = TimeUnit.getTimeValue(accessValidTimeUnit.toLowerCase()) * parsedAccessValidTime;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid access token valid time: " + accessValidTime + "\nPlease check jwt.validate.time.access at properties");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid access token time unit:" + accessValidTimeUnit + "\nPlease check jwt.validate.time.access.unit at properties" + "\nValid values: [second,minute,hour,day,week]");
        }

        try {
            int parsedRefreshValidTime = Integer.parseInt(refreshValidTime);
            this.REFRESH_TOKEN_VALID_TIME = TimeUnit.getTimeValue(refreshValidTimeUnit.toLowerCase()) * parsedRefreshValidTime;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Refresh Token Valid Time: " + refreshValidTime + "\nPlease check jwt.validate.time.refresh at properties");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid Refresh Token Time Unit:" + refreshValidTimeUnit + "\nPlease check jwt.validate.time.refresh.unit at properties" + "\nValid values: [second,minute,hour,day,week]");
        }

        try {
            this.secretKey = Base64.getEncoder().encodeToString(rawSecretKey.getBytes());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid JWT secret key: " + rawSecretKey + "\nPlease check jwt.signing.secret at properties.");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("No represented JWT secret key.\nPlease write jwt.signing.secret at properties.");
        }

    }


    public String createToken(User user, JwtType type) {
        Date now = new Date();
        Date expiredAt = new Date(type.equals(JwtType.ACCESS) ? now.getTime() + ACCESS_TOKEN_VALID_TIME : now.getTime() + REFRESH_TOKEN_VALID_TIME);
        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .subject(user.getUsername())
                .claims(Map.of("type", type))
                .issuedAt(now)
                .expiration(expiredAt)
                .compact();
        if (type.equals(JwtType.REFRESH)) {
            saveRefreshToken(user, token, expiredAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        return token;
    }


    private void saveRefreshToken(User user, String token, LocalDateTime expiredAt) {
        RefreshToken refreshToken = RefreshToken.builder().user(user).token(token).expiredAt(expiredAt).build();
        refreshTokenRepository.save(refreshToken);
    }

    public Date getExpiration(String token) {
        return getClaims(token).getPayload().getExpiration();
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parser().decryptWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseSignedClaims(token);
    }

    public TokenPackage extractToken(HttpServletRequest httpReq) {
        return new TokenPackage(httpReq);
    }

    public void invalidateRefreshToken(String token) {
        INVALIDATED_REFRESH_TOKEN.put(token, true, getExpiration(token));
    }

    public boolean isValidate(String token) {
        try {
            return !getClaims(token).getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isInvalidatedToken(TokenPackage tokenPackage) {
        return INVALIDATED_REFRESH_TOKEN.contains(tokenPackage.getRefreshToken());
    }

    public void clearJwtCookies(HttpServletResponse httpRes) {
        Cookie accessCookie = new Cookie(JwtType.ACCESS.getCookieName(), null);
        accessCookie.setMaxAge(0);
        accessCookie.setPath("/");
        accessCookie.setHttpOnly(true);
        httpRes.addCookie(accessCookie);
        Cookie refreshCookie = new Cookie(JwtType.REFRESH.getCookieName(), null);
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        httpRes.addCookie(refreshCookie);
    }

    public User extractUserFromToken(String token) {
        String username = getClaims(token).getPayload().getSubject();
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElseThrow(() ->
                new UsernameNotFoundException("The token was parsed successfully, but a non-existent username was returned. Caused By: " + username)
        );
    }

    public void setJwtCookie(HttpServletResponse httpRes, String token, JwtType type) {
        Cookie tokenCookie = new Cookie(type.getCookieName(), token);
        tokenCookie.setPath("/");
        tokenCookie.setHttpOnly(true);
        httpRes.addCookie(tokenCookie);
    }
}
