package com.hyeonmusic.MySongSpace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String username;
    private String email;
    private String nickname;
    private String password;
    private String profilePicture;
    private LocalDateTime createdAt;
    private String memberKey; // 추가: 고유 키

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role; // 추가: 역할

    @OneToMany(mappedBy = "member") // mappedBy도 변경
    private List<Track> tracks=new ArrayList<>();

    @OneToMany(mappedBy = "member") // mappedBy도 변경
    private List<Album> albums=new ArrayList<>();

    // 필요한 경우 getter와 setter 추가

    // 생성자에서 createdAt 초기화
    public Member(String username, String email, String nickname, String password, String profilePicture) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profilePicture = profilePicture;
        this.createdAt = LocalDateTime.now(); // 현재 시간으로 생성
    }

    // 소셜 로그인 사용자 생성자
    public Member(String username, String email, String profilePicture, String memberKey, Role role) {
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.memberKey = memberKey;
        this.role = role;
        this.createdAt = LocalDateTime.now(); // 현재 시간으로 생성
    }
}


