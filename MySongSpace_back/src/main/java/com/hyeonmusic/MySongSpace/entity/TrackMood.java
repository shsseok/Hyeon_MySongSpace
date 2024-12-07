package com.hyeonmusic.MySongSpace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class TrackMood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id")
    private Track track;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Mood mood;

    public TrackMood(Track track, Mood mood) {
        this.track = track;
        this.mood = mood;
    }

    private static TrackMood createTrackMood(Track track, Mood mood) {
        TrackMood trackMood = new TrackMood(track, mood);
        return trackMood;
    }

    public static List<TrackMood> createTrackMoodList(Track track, List<Mood> moods) {
        List<TrackMood> trackMoods = new ArrayList<>();
        moods.forEach(mood -> {
            trackMoods.add(createTrackMood(track, mood));
        });
        return trackMoods;
    }

}
