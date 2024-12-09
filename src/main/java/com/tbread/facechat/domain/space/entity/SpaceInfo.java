package com.tbread.facechat.domain.space.entity;

import com.tbread.facechat.domain.common.Timestamped;
import com.tbread.facechat.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SpaceInfo extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id",unique = true)
    private Space space;

    @Builder
    public SpaceInfo(final User user, final Space space) {
        this.user = user;
        this.space = space;
    }

}
