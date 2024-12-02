package com.tbread.facechat.domain.user.entity;

import com.tbread.facechat.domain.common.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "user_nickname")
public class UserNickname extends Timestamped {
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

    @Builder
    public UserNickname(User user,String nickname){
        this.user = user;
        this.nickname = nickname;
    }

    public String getNickname(){
        return this.nickname;
    }
}
