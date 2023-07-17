package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Event {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int _iId;

    @NotNull
    @Column(name = "title")
    private String _sTitle;

    @NotNull
    @Column(name = "celebratedAt")
    private LocalDateTime _tCelebratedAt;

    @Column(name = "description")
    private String _sDescription;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User _organizer;

    @NotNull
    @ManyToMany
    @JoinColumn(name = "assistant_id")
    private Set<User> _setAssistants;

    @ManyToMany
    @JoinColumn(name = "interest_id")
    private Set<Interest> _setInterest;

    @OneToOne
    @JoinColumn(name = "headerphoto_id")
    private ImagePath _headerPhoto;

    @ManyToOne
    @JoinColumn(name = "localization_id")
    private Localization _localization;

    public int get_iId() {
        return _iId;
    }

    public String get_sTitle() {
        return _sTitle;
    }

    public void set_sTitle(String sTitle) {
        _sTitle = sTitle;
    }

    public LocalDateTime get_tCelebratedAt() {
        return _tCelebratedAt;
    }

    public void set_tCelebratedAt(LocalDateTime tCelebratedAt) {
        _tCelebratedAt = tCelebratedAt;
    }

    public String get_sDescription() {
        return _sDescription;
    }

    public void set_sDescription(String sDescription) {
        _sDescription = sDescription;
    }

    public User get_organizer() {
        return _organizer;
    }

    public void set_organizer(User organizer) {
        _organizer = organizer;
    }

    public Set<User> get_setAssistants() {
        return _setAssistants;
    }

    public void set_setAssistants(Set<User> setAssistants) {
        _setAssistants = setAssistants;
    }

    public Set<Interest> get_setInterest() {
        return _setInterest;
    }

    public void set_setInterest(Set<Interest> setInterest) {
        _setInterest = setInterest;
    }

    public ImagePath get_headerPhoto() {
        return _headerPhoto;
    }

    public void set_headerPhoto(ImagePath headerPhoto) {
        _headerPhoto = headerPhoto;
    }

    public Localization get_localization() {
        return _localization;
    }

    public void set_localization(Localization localization) {
        _localization = localization;
    }
}
