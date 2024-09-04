package com.tbread.facechat.domain;

import com.tbread.facechat.domain.authentication.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@RequiredArgsConstructor
@Controller
public class GlobalMappingController {

    @RequestMapping("account/sign_in")
    String login(){
        return "login";
    }
    //로그인

    @RequestMapping("account/signup")
    String signup(){return "signup";}
    //회원가입

    @RequestMapping("")
    String main(@AuthenticationPrincipal UserDetailsImpl userDetails){
        if (Objects.isNull(userDetails)){
            return "redirect:account/sign_in";
        }
        return "main";}
}
