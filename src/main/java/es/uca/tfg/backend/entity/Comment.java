package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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
}
