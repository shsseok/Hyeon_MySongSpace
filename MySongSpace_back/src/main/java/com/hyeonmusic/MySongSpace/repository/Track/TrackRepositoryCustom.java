package com.hyeonmusic.MySongSpace.repository.Track;

import com.hyeonmusic.MySongSpace.entity.Genre;
import com.hyeonmusic.MySongSpace.entity.Mood;
import com.hyeonmusic.MySongSpace.entity.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackRepositoryCustom {
    Page<Track> findTracksWithFilters(@Param("moods") List<Mood> moods,
                                      @Param("genres") List<Genre> genres,
                                      @Param("sortBy") String sortBy,
                                      Pageable pageable);
}
