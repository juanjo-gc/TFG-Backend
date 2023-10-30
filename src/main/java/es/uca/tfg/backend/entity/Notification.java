package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int _iId;

    @NotNull
    @Column(name = "info")
    private String _sInfo;

    @NotNull
    @Column(name = "seen")
    private boolean _bSeen;

    @NotNull
    @Column(name = "createdAt")
    private LocalDateTime _tCreatedAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User _recipient;

    @ManyToOne
    @JoinColumn(name = "issuer_id")
    private Person _issuer;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post _post;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event _event;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "type_id")
    private TypeNotification _type;



    public Notification() {}

    //Tipo 1: Solo issuer
    public Notification(String sInfo, User recipient, TypeNotification type, Person issuer) {
        _sInfo = sInfo;
        _recipient = recipient;
        _bSeen = false;
        _tCreatedAt = LocalDateTime.now();
        _type = type;
        _issuer = issuer;
        _event = null;
        _post = null;
    }
    //Tipo 2: issuer y event
    public Notification(String sInfo, User recipient, TypeNotification type, Person issuer, Event event) {
        _sInfo = sInfo;
        _recipient = recipient;
        _bSeen = false;
        _tCreatedAt = LocalDateTime.now();
        _type = type;
        _issuer = issuer;
        _event = event;
        _post = null;
    }
    //Tipo 3: Nada
    public Notification(String sInfo, User recipient, TypeNotification type) {
        _sInfo = sInfo;
        _recipient = recipient;
        _bSeen = false;
        _tCreatedAt = LocalDateTime.now();
        _type = type;
        _issuer = null;
        _event = null;
        _post = null;
    }
    //Tipo 4: issuer y Post
    public Notification(String sInfo, User recipient, TypeNotification type, Person issuer, Post post) {
        _sInfo = sInfo;
        _recipient = recipient;
        _bSeen = false;
        _tCreatedAt = LocalDateTime.now();
        _type = type;
        _issuer = issuer;
        _event = null;
        _post = post;
    }

    public int get_iId() {
        return _iId;
    }

    public String get_sInfo() {
        return _sInfo;
    }

    public boolean is_bSeen() {
        return _bSeen;
    }

    public LocalDateTime get_tCreatedAt() {
        return _tCreatedAt;
    }

    public Event get_event() {
        return _event;
    }

    public User get_recipient() {
        return _recipient;
    }

    public Person get_issuer() {
        return _issuer;
    }

    public Post get_post() {
        return _post;
    }

    public Event get_Event() {
        return _event;
    }

    public TypeNotification get_type() {
        return _type;
    }
}
