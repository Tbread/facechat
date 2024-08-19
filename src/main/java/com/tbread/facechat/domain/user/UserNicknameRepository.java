package com.tbread.facechat.domain.user;

import com.tbread.facechat.domain.user.entity.UserNickname;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserNicknameRepository extends JpaRepository<UserNickname,Long> {
    Optional<UserNickname> findByNickname(String nickname);
    boolean existsByNickname(String nickname);
}
