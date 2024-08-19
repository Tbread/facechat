package com.tbread.facechat.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserNicknameRepository userNicknameRepository;

    private final PasswordEncoder passwordEncoder;
}
