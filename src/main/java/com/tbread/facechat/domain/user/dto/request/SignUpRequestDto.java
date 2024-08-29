package com.tbread.facechat.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequestDto(@Email(message = "이메일 형식이 아닙니다.") String username,
                               @NotBlank(message = "공백일 수 없습니다.") String password,
                               @NotBlank(message = "공백일 수 없습니다.") String nickname) {
}
