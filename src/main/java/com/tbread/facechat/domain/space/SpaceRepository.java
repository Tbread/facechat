package com.tbread.facechat.domain.space;

import com.tbread.facechat.domain.space.entity.Space;
import com.tbread.facechat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpaceRepository extends JpaRepository<Space, UUID> {
    Optional<Space> findById(UUID uuid);
    int countAllByUser(User user);
    List<Space> findAllByUserAndValid(User user, boolean valid);
}
