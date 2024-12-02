package com.tbread.facechat.domain;

import com.tbread.facechat.domain.authentication.userdetails.UserDetailsImpl;
import com.tbread.facechat.domain.user.UserNicknameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@RequiredArgsConstructor
@Controller
public class GlobalMappingController {

    private final UserNicknameRepository nicknameRepository;

    @RequestMapping("account/sign_in")
    String login(){
        return "login";
    }
    //로그인

    @RequestMapping("account/signup")
    String signup(){return "signup";}
    //회원가입

    @RequestMapping("")
    String main(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model){
        if (Objects.isNull(userDetails)){
            return "redirect:account/sign_in";
        }
        model.addAttribute("nickname",nicknameRepository.findByUser(userDetails.getUser()).get().getNickname());
        return "main";}
}
