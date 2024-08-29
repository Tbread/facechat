package com.tbread.facechat.domain.user;

import com.tbread.facechat.domain.authentication.jwt.JwtProcessor;
import com.tbread.facechat.domain.common.Result;
import com.tbread.facechat.domain.user.dto.request.UsernameAndPasswordRequestDto;
import com.tbread.facechat.domain.user.dto.response.LoginResponseDto;
import com.tbread.facechat.domain.user.entity.User;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserNicknameRepository userNicknameRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtProcessor jwtProcessor;

    @Transactional
    public Result<LoginResponseDto> login(HttpServletResponse httpRes, UsernameAndPasswordRequestDto req) {
        Optional<User> userOptional = userRepository.findByUsername(req.username());
        if (userOptional.isEmpty()) {
            return new Result<>("잘못된 아이디 또는 패스워드입니다.", HttpStatus.BAD_REQUEST, false);
        }
        if (!passwordEncoder.matches(req.password(), userOptional.get().getPassword())) {
            return new Result<>("잘못된 아이디 또는 패스워드입니다.", HttpStatus.BAD_REQUEST, false);
        }
        String refreshToken = jwtProcessor.createToken(userOptional.get(), JwtProcessor.JwtType.REFRESH);
        String accessToken = jwtProcessor.createToken(userOptional.get(), JwtProcessor.JwtType.ACCESS);

        jwtProcessor.setJwtCookie(httpRes,accessToken, JwtProcessor.JwtType.ACCESS);
        jwtProcessor.setJwtCookie(httpRes,refreshToken, JwtProcessor.JwtType.REFRESH);
        //백엔드 - 프론트서버의 비 분리 사용으로 편의성을 위해 쿠키로 설정, 추후 필터체인으로 쿠키 관리 필요
        userOptional.get().updateLastLoginAt();
        return new Result<>(HttpStatus.OK, new LoginResponseDto(refreshToken, accessToken), true);
    }

    public Result logout(HttpServletRequest httpReq, HttpServletResponse httpRes) {
        jwtProcessor.clearJwtCookies(httpRes);
        String token = jwtProcessor.extractToken(httpReq).getRefreshToken();
        try {
            jwtProcessor.invalidateRefreshToken(token);
        } catch (JwtException | IllegalArgumentException ignored) {
        }
        return new Result<>(HttpStatus.OK,true);
    }
}
