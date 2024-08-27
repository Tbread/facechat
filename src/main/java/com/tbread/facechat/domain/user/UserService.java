package com.tbread.facechat.domain.user;

import com.tbread.facechat.domain.authentication.jwt.JwtProcessor;
import com.tbread.facechat.domain.common.Result;
import com.tbread.facechat.domain.user.dto.request.UsernameAndPasswordRequestDto;
import com.tbread.facechat.domain.user.dto.response.LoginResponseDto;
import com.tbread.facechat.domain.user.entity.User;
import com.tbread.facechat.util.ExpiringHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserNicknameRepository userNicknameRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtProcessor jwtProcessor;

    private final static ExpiringHashMap<String,Boolean> INVALIDATED_REFRESH_TOKEN = new ExpiringHashMap<>();

    public Result<LoginResponseDto> login(UsernameAndPasswordRequestDto req){
        Optional<User> userOptional = userRepository.findByUsername(req.username());
        if (userOptional.isEmpty()){
            return new Result<>("잘못된 아이디 또는 패스워드입니다.",HttpStatus.BAD_REQUEST,false);
        }
        if (!passwordEncoder.matches(req.password(),userOptional.get().getPassword())){
            return new Result<>("잘못된 아이디 또는 패스워드입니다.",HttpStatus.BAD_REQUEST,false);
        }
        String refreshToken = jwtProcessor.createToken(userOptional.get(), JwtProcessor.JwtType.REFRESH);
        String accessToken = jwtProcessor.createToken(userOptional.get(), JwtProcessor.JwtType.ACCESS);
        
        return new Result<>(HttpStatus.OK,new LoginResponseDto(refreshToken,accessToken),true);
    }
}
