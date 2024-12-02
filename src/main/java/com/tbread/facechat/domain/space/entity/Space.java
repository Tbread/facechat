package com.tbread.facechat.domain.space.entity;

import com.tbread.facechat.domain.common.Timestamped;
import com.tbread.facechat.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Space extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",unique = true)
    private User user;

    @Column(nullable = false)
    private boolean extend;

    @Column(nullable = false)
    private boolean valid;

    @Builder
    public Space(User user, boolean extend) {
        this.user = user;
        this.extend = extend;
        this.valid = true;
    }

    public void modifyExtend(boolean extend) {
        this.extend = extend;
    }

    public void expiration(){
        this.valid = false;
    }
}
