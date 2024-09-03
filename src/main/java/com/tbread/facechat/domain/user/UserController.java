package com.tbread.facechat.domain.user;

import com.tbread.facechat.domain.common.Result;
import com.tbread.facechat.domain.user.dto.request.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("signup")
    public ResponseEntity<Result> signup(@RequestBody SignUpRequestDto req){
        return userService.signUp(req).publish();
    }

}
