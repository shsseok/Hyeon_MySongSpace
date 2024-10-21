package com.hyeonmusic.MySongSpace.repository;


import com.hyeonmusic.MySongSpace.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long>, TrackRepositoryCustom {

}
