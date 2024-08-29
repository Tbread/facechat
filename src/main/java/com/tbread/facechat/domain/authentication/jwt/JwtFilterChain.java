package com.tbread.facechat.domain.authentication.jwt;

import com.tbread.facechat.domain.common.TokenPackage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
public class JwtFilterChain extends GenericFilterBean {
    private final JwtProcessor jwtProcessor;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;
        TokenPackage tokenPackage = jwtProcessor.extractToken(httpReq);
        if (Objects.nonNull(tokenPackage.getRefreshToken()) && Objects.nonNull(tokenPackage.getAccessToken())) {
            if (jwtProcessor.isValidate(tokenPackage.getAccessToken())) {
                //액세스 토큰 비만료
                Authentication authentication = jwtProcessor.getAuthentication(tokenPackage.getAccessToken());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                //액세스 토큰 만료
                if (jwtProcessor.isValidate(tokenPackage.getRefreshToken()) && !jwtProcessor.isInvalidatedToken(tokenPackage)) {
                    //리프레시 토큰 비만료
                    String newAccessToken = jwtProcessor.createToken(jwtProcessor.extractUserDetails(tokenPackage.getRefreshToken()).getUser(), JwtProcessor.JwtType.ACCESS);
                    jwtProcessor.setJwtCookie(httpRes, newAccessToken, JwtProcessor.JwtType.ACCESS);
                    Authentication authentication = jwtProcessor.getAuthentication(newAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    //리프레시 토큰 만료
                    jwtProcessor.clearJwtCookies(httpRes);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
