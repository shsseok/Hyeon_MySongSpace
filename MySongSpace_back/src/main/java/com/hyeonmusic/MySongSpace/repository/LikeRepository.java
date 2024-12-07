package com.hyeonmusic.MySongSpace.repository;

import com.hyeonmusic.MySongSpace.entity.Likes;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {
    Likes findByTrackAndMember(Track track, Member member);
}
