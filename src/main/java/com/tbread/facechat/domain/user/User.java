package com.tbread.facechat.domain.user;

import com.tbread.facechat.domain.common.Timestamped;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
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

}
