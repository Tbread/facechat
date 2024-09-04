package com.tbread.facechat.domain.authentication.jwt.entity;

import com.tbread.facechat.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiredAt;
    //추후 mysql에서 해당 컬럼 기반으로 제거 스케쥴러 설정 혹은 자체 스케쥴러 클래스 작성 필요

    @Builder
    public RefreshToken(User user,String token,LocalDateTime expiredAt){
        this.user = user;
        this.token = token;
        this.expiredAt = expiredAt;
    }
}
