package com.tbread.facechat.domain.authentication.jwt;

import com.tbread.facechat.domain.authentication.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    boolean existsByTokenAndExpiredAtGreaterThan(String token, LocalDateTime expiredAt);
}
