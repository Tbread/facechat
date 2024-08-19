package com.tbread.facechat.domain.user;

import com.tbread.facechat.domain.user.entity.UserNickname;
import com.tbread.facechat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserNicknameRepository extends JpaRepository<UserNickname,Long> {
    Optional<User> findByNickname(Long userId);
}
