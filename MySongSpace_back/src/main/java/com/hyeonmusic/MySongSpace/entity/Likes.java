package com.hyeonmusic.MySongSpace.entity;

import jakarta.persistence.*;



@Entity
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne // 멤버와의 관계 설정
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne // 트랙과의 관계 설정
    @JoinColumn(name = "track_id")
    private Track track;

    // Getters and Setters
}


