package com.hyeonmusic.MySongSpace.repository;

import com.hyeonmusic.MySongSpace.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByMemberKey(String memberKey);
}

