package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int _iId;

    @NotNull
    @Column(name = "text")
    private String _sText;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User _user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event _event;

    @NotNull
    @Column(name = "datetime")
    private LocalDateTime _tDatetime;

    @Column(name = "deleteDate")
    private LocalDateTime _tDeleteDate;

    public Comment(String sText, User user, Event event) {
        _sText = sText;
        _user = user;
        _event = event;
        _tDatetime = LocalDateTime.now();
        _tDeleteDate = null;
    }

    public Comment() {}

    public int get_iId() {
        return _iId;
    }

    public String get_sText() {
        return _sText;
    }

    public void set_sText(String sText) {
        _sText = sText;
    }

    public User get_user() {
        return _user;
    }

    public void set_user(User user) {
        _user = user;
    }

    public Event get_event() {
        return _event;
    }

    public void set_event(Event event) {
        _event = event;
    }

    public LocalDateTime get_tDatetime() {
        return _tDatetime;
    }

    public LocalDateTime get_tDeleteDate() { return _tDeleteDate; }

    public void set_tDeleteDate(LocalDateTime tDeleteDate) { _tDeleteDate = tDeleteDate; }
}
