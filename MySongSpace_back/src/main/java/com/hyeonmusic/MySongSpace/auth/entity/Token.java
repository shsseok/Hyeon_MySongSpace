package com.hyeonmusic.MySongSpace.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @Column(unique = true)
    private String memberKey;

    private String refreshToken;

    private String accessToken;

    public Token(String memberKey, String refreshToken, String accessToken) {
        this.memberKey = memberKey;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
