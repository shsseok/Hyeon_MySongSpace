package com.hyeonmusic.MySongSpace.repository;

import com.hyeonmusic.MySongSpace.auth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {

    Optional<Token> findByAccessToken(String accessToken);
    void deleteByMemberKey(String memberKey);
}
