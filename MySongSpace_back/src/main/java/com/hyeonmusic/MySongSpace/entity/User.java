package com.hyeonmusic.MySongSpace.entity;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDateTime;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String email;
    private String nickname;
    private String password;
    private String profilePicture;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<Track> tracks;

    @OneToMany(mappedBy = "user")
    private List<Album> albums;



}
