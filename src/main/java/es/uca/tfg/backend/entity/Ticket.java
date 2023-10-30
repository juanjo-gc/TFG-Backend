package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
public class Ticket {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int _iId;

    @NotNull
    @Column(name = "creationDate")
    private LocalDateTime _tCreationDate;

    @NotNull
    @Column(name = "subject")
    private String _sSubject;

    @Column(name = "description")
    private String _sDescription;

    @NotNull
    @Column(name = "isOpen")
    private boolean _bIsOpen;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin _admin;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "issuer_id")
    private User _issuer;

    @OneToOne
    @JoinColumn(name = "image_id")
    private ImagePath _imagePath;

    @ManyToOne
    @JoinColumn(name = "reported_id")
    private User _reported;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event _event;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post _post;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category _category;
    
    public Ticket() {}

    public Ticket(String sSubject, String sDescription, Admin admin, User issuer, User reported, Category category) {
        _tCreationDate = LocalDateTime.now();
        _bIsOpen = true;
        _sSubject = sSubject;
        _sDescription = sDescription;
        _admin = admin;
        _issuer = issuer;
        _reported = reported;
        _event = null;
        _post = null;
        _category = category;
    }


    public Ticket(String sSubject, String sDescription, Admin admin, User issuer, Category category) {
        _tCreationDate = LocalDateTime.now();
        _bIsOpen = true;
        _sSubject = sSubject;
        _sDescription = sDescription;
        _admin = admin;
        _issuer = issuer;
        _reported = null;
        _event = null;
        _post = null;
        _category = category;
    }

    public Ticket(String sSubject, String sDescription, Admin admin, User issuer, User reported, Event event, Post post, Category category) {
        _tCreationDate = LocalDateTime.now();
        _bIsOpen = true;
        _sSubject = sSubject;
        _sDescription = sDescription;
        _admin = admin;
        _issuer = issuer;
        _reported = reported;
        _event = event;
        _post = post;
        _category = category;
    }



    public int get_iId() {
        return _iId;
    }

    public LocalDateTime get_tCreationDate() {
        return _tCreationDate;
    }

    public String get_sSubject() {
        return _sSubject;
    }

    public String get_sDescription() {
        return _sDescription;
    }

    public boolean is_bIsOpen() {
        return _bIsOpen;
    }

    public Admin get_admin() {
        return _admin;
    }

    public User get_issuer() {
        return _issuer;
    }

    public ImagePath get_imagePath() {
        return _imagePath;
    }

    public void set_imagePath(ImagePath imagePath) {
        _imagePath = imagePath;
    }

    public void set_bIsOpen(boolean bIsOpen) {
        _bIsOpen = bIsOpen;
    }

    public User get_reported() {
        return _reported;
    }

    public Event get_event() {
        return _event;
    }

    public Post get_post() {
        return _post;
    }

    public Category get_category() { return _category; }

    public void set_category(Category category) { _category = category; }
}
