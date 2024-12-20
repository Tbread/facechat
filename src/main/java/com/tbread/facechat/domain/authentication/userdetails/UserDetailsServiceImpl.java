package com.tbread.facechat.domain.authentication.userdetails;

import com.tbread.facechat.domain.user.UserRepository;
import com.tbread.facechat.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()){
            throw new UsernameNotFoundException("The token was parsed successfully, but a non-existent username was returned. Caused By: " + username);
        }
        return new UserDetailsImpl(userOptional.get());
    }
}
