package es.uca.tfg.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

@Entity
public class Post {
    @Id
    @GeneratedValue
    private int _iId;

    @NotNull
    @Column(name = "text")
    private String _sText;

    private int _iLikes;

    private int _iReplies;

    @NotNull
    @Column(name = "createdAt")
    private LocalDateTime _tCreatedAt;

    @Column(name = "deleteDate")
    private LocalDateTime _tDeleteDate;

    @NotNull
    @ManyToOne
    private User _user;

    @OneToMany(mappedBy = "_repliesTo")
    @JsonIgnore
    private Set<Post> _setReplies;

    @ManyToOne
    private Post _repliesTo;

    @NotNull
    @ManyToMany
    @JoinTable(
            name = "liked_user",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    private Set<User> _setLikes;


    public Post() {}

    public Post(String sText, User user) {
        _sText = sText;
        _user = user;
        _tCreatedAt = LocalDateTime.now();
        _setLikes = Collections.emptySet();
        _setReplies = Collections.emptySet();
        _iLikes = 0;
        _iReplies = 0;
        _repliesTo = null;
        _tDeleteDate = null;
    }

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

    public Set<Post> get_setReplies() {
        return _setReplies;
    }

    public void set_setReplies(Set<Post> replies) {
        _setReplies = replies;
    }

    public Post get_repliesTo() {
        return _repliesTo;
    }

    public void set_repliesTo(Post _repliesTo) {
        this._repliesTo = _repliesTo;
    }

    public Set<User> get_setLikes() {
        return _setLikes;
    }

    public void set_setLikes(Set<User> setLikes) {
        _setLikes = setLikes;
    }

    public int get_iLikes() {
        return _iLikes;
    }

    public void set_iLikes(int _iLikes) {
        this._iLikes = _iLikes;
    }

    public int get_iReplies() { return _iReplies; }

    public void set_iReplies(int iReplies) { _iReplies = iReplies; }

    public LocalDateTime get_tDeleteDate() { return _tDeleteDate; }

    public void set_tDeleteDate(LocalDateTime tDeleteDate) { _tDeleteDate = tDeleteDate; }

    public static class PostComparator implements Comparator<Post> {
        @Override
        public int compare(Post o1, Post o2) {
            return o2._tCreatedAt.compareTo(o1._tCreatedAt);    //De esta forma la publciación más reciente es la primera
        }
    }
}
