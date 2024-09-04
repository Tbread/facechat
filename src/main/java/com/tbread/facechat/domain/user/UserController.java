package com.tbread.facechat.domain.user;

import com.tbread.facechat.domain.authentication.jwt.JwtProcessor;
import com.tbread.facechat.domain.common.Result;
import com.tbread.facechat.domain.user.dto.request.SignUpRequestDto;
import com.tbread.facechat.domain.user.dto.request.UsernameAndPasswordRequestDto;
import com.tbread.facechat.domain.user.dto.response.LoginResponseDto;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtProcessor jwtProcessor;

    @PostMapping("signup")
    public ResponseEntity<Result> signup(@RequestBody SignUpRequestDto req){
        return userService.signUp(req).publish();
    }

    @PostMapping("sign_in")
    public ResponseEntity<Result<LoginResponseDto>> signIn(HttpServletResponse httpRes, @RequestBody UsernameAndPasswordRequestDto req){
        Result<LoginResponseDto> res = userService.login(req);
        httpRes.addCookie(jwtProcessor.setJwtCookie(Objects.isNull(res.getData()) ? null : res.getData().accessToken(), JwtProcessor.JwtType.ACCESS));
        httpRes.addCookie(jwtProcessor.setJwtCookie(Objects.isNull(res.getData()) ? null : res.getData().refreshToken(), JwtProcessor.JwtType.REFRESH));
        return res.publish();
    }

    @GetMapping("logout")
    public void logout(HttpServletRequest httpReq,HttpServletResponse httpRes){
        httpRes.addCookie(jwtProcessor.clearAccessCookie());
        httpRes.addCookie(jwtProcessor.clearRefreshCookie());
        String token = jwtProcessor.extractToken(httpReq).getRefreshToken();
        try {
            jwtProcessor.invalidateRefreshToken(token);
        } catch (JwtException | IllegalArgumentException ignored) {
        }
    }

}
