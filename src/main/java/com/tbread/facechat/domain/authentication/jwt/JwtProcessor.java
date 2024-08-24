package com.tbread.facechat.domain.authentication.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;
import java.util.Set;

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

    private final long ACCESS_TOKEN_VALID_TIME;
    private final long REFRESH_TOKEN_VALID_TIME;

    private final String secretKey;

    public JwtProcessor(@Value("${jwt.validate.time.access:#{null}}") String accessValidTime,
                        @Value("${jwt.validate.time.access.unit:#{null}}") String accessValidTimeUnit,
                        @Value("${jwt.validate.time.refresh:#{null}}") String refreshValidTime,
                        @Value("${jwt.validate.time.refresh.unit:#{null}}") String refreshValidTimeUnit,
                        @Value("${jwt.signing.secret:#{null}}") String rawSecretKey) {

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
}
