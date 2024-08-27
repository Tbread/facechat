package com.tbread.facechat.domain.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public class TokenPackage {
    private final String accessToken;
    private final String refreshToken;

    public TokenPackage(HttpServletRequest httpReq){
        if (httpReq.getCookies() != null) {
            Optional<Cookie> cookieOptional = Arrays.stream(httpReq.getCookies()).filter(c -> c.getName().equals("Refresh-Token")).findFirst();
            this.refreshToken = cookieOptional.map(Cookie::getValue).orElse(null);
            cookieOptional = Arrays.stream(httpReq.getCookies()).filter(c -> c.getName().equals("Access-Token")).findFirst();
            this.accessToken = cookieOptional.map(Cookie::getValue).orElse(null);
        } else {
            this.refreshToken = null;
            this.accessToken = null;
        }
    }
}
