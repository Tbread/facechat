package com.tbread.facechat.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
