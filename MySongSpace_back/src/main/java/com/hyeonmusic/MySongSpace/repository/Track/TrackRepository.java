package com.hyeonmusic.MySongSpace.repository.Track;


import com.hyeonmusic.MySongSpace.entity.Track;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long>, TrackRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Track t WHERE t.trackId = :trackId ")
    Optional<Track> findByIdWithLock(@Param("trackId") Long trackId);

    @Query(value = "SELECT t.* FROM track t " +
            "JOIN member m ON t.member_id = m.member_id " +
            "LEFT JOIN track_genre tg ON t.track_id = tg.track_id " +
            "LEFT JOIN track_mood tm ON t.track_id = tm.track_id " +
            "WHERE MATCH(t.title, t.description) AGAINST (?1 IN BOOLEAN MODE) " +
            "ORDER BY t.uploaded_at DESC", nativeQuery = true)
    List<Track> findTracksWithKeyword(@Param("keyword") String keyword);

//    @Lock(LockModeType.OSPTIMISTIC) // 기본 낙관적 락
//    @Query("SELECT t FROM Track t WHERE t.trackId = :trackId")
//    Optional<Track> findByIdWithLock(@Param("trackId") Long trackId);
}
