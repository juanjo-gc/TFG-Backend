package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

public class Post {
    @Id
    @GeneratedValue
    private int _iId;

    @NotNull
    @Column(name = "text")
    private String _sText;

    @NotNull
    @Column(name = "createdAt")
    private LocalDateTime _tCreatedAt;

    @NotNull
    @ManyToOne
    private User _user;

    @OneToMany
    private Set<Post> _replies;

    @NotNull
    @Column(name = "likes")
    private int _iLikes;


    public int get_iId() {
        return _iId;
    }

    public String get_sText() {
        return _sText;
    }

    public void set_sText(String sText) {
        _sText = sText;
    }

    public LocalDateTime get_tCreatedAt() {
        return _tCreatedAt;
    }

    public User get_user() {
        return _user;
    }

    public void set_user(User user) {
        _user = user;
    }

    public Set<Post> get_replies() {
        return _replies;
    }

    public void set_replies(Set<Post> replies) {
        _replies = replies;
    }

    public int get_iLikes() {
        return _iLikes;
    }

    public void set_iLikes(int iLikes) {
        _iLikes = iLikes;
    }
}
