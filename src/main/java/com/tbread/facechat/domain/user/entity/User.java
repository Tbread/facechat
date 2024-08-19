package com.tbread.facechat.domain.user.entity;

import com.tbread.facechat.domain.common.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private LocalDateTime lastLoginAt;

    @Builder
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void updateLastLoginAt(){
        this.lastLoginAt = LocalDateTime.now();
    }

    public void updatePassword(String password){
        this.password = password;
    }
}
