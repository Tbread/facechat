package com.tbread.facechat.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class GlobalMappingController {

    @RequestMapping("login")
    String login(){
        return "login";
    }
}
