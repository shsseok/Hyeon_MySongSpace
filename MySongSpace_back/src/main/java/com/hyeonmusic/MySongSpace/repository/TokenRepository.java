package com.hyeonmusic.MySongSpace.repository;

import com.hyeonmusic.MySongSpace.auth.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


public interface TokenRepository extends CrudRepository<Token, String> {

    Optional<Token> findByAccessToken(String accessToken);

    void deleteByMemberKey(String memberKey);
}
