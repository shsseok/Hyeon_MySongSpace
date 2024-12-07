package com.hyeonmusic.MySongSpace.repository.Track;


import com.hyeonmusic.MySongSpace.entity.Track;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long>, TrackRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Track t WHERE t.trackId = :trackId ")
    Optional<Track> findByIdWithLock(@Param("trackId") Long trackId);

//    @Lock(LockModeType.OSPTIMISTIC) // 기본 낙관적 락
//    @Query("SELECT t FROM Track t WHERE t.trackId = :trackId")
//    Optional<Track> findByIdWithLock(@Param("trackId") Long trackId);
}
