package com.tbread.facechat.domain.user.entity;

import com.tbread.facechat.domain.common.Timestamped;
import jakarta.persistence.*;

@Entity
@Table(name = "user_nickname")
public class Nickname extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",unique = true)
    private User user;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
