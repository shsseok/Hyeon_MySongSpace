package com.hyeonmusic.MySongSpace.auth.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
//JPA Id가 아닌 이거를 import 해야한다
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@AllArgsConstructor
@RedisHash(value = "jwt", timeToLive = 60 * 60 * 7 )
public class Token {

    @Id
    private String tokenId;

    private String refreshToken;

    @Indexed
    private String accessToken;

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
