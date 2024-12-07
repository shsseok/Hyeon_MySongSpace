package com.hyeonmusic.MySongSpace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class TrackGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id")
    private Track track;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;

    public TrackGenre(Genre genre, Track track) {
        this.track = track;
        this.genre = genre;
    }

    private static TrackGenre createTrackGenre(Track track, Genre genre) {
        TrackGenre trackGenre = new TrackGenre(genre, track);
        return trackGenre;
    }

    public static List<TrackGenre> createTrackGenreList(Track track, List<Genre> genres) {
        List<TrackGenre> trackGenres = new ArrayList<>();
        genres.forEach(genre -> {
            trackGenres.add(createTrackGenre(track, genre));
        });
        return trackGenres;
    }
}

