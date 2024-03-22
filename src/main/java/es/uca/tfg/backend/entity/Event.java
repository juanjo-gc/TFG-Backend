package es.uca.tfg.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
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
    private LocalDate _tCelebratedAt;

    @NotNull
    @Column(name = "celebrationHour")
    private LocalTime _tCelebrationHour;

    @Column(name = "description", columnDefinition = "BLOB")
    private String _sDescription;

    @Column(name = "deleteDate")
    private LocalDateTime _tDeleteDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User _organizer;

    @NotNull
    @ManyToMany
    @JoinTable(
            name = "event_assistant",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> _setAssistants;

    @ManyToMany
    @JoinColumn(name = "interest_id")
    @JsonIgnore
    private Set<Interest> _setInterests;

    @OneToOne
    @JoinColumn(name = "headerphoto_id")
    private ImagePath _headerPhoto;

    @OneToMany
    @JoinColumn(name = "photos")
    @JsonIgnore
    private Set<ImagePath> _setPhotos;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location _location;

    @Column(name = "isOnline")
    private boolean _bIsOnline;

    public Event() {}

    public Event(String sTitle, LocalDate tCelebratedAt, LocalTime tCelebrationHour, String sDescription, User organizer,
                 Set<Interest> setInterests, ImagePath headerPhoto, Location location, boolean bIsOnline) {
        _sTitle = sTitle;
        _tCelebratedAt = tCelebratedAt;
        _tCelebrationHour = tCelebrationHour;
        _sDescription = sDescription;
        _organizer = organizer;
        _headerPhoto = headerPhoto;
        _location = location;
        if(setInterests != null)
            _setInterests = setInterests;
        else
            _setInterests = new HashSet<>();
        _setAssistants = new HashSet<>();
        _bIsOnline = bIsOnline;
        _tDeleteDate = null;
    }

    public int get_iId() {
        return _iId;
    }

    public String get_sTitle() {
        return _sTitle;
    }

    public void set_sTitle(String sTitle) {
        _sTitle = sTitle;
    }

    public LocalDate get_tCelebratedAt() {
        return _tCelebratedAt;
    }

    public LocalTime get_tCelebrationHour() {
        return _tCelebrationHour;
    }

    public void set_tCelebratedAt(LocalDate tCelebratedAt) {
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
        return _setInterests;
    }

    public void set_setInterest(Set<Interest> setInterest) {
        _setInterests = setInterest;
    }

    public Set<ImagePath> get_setPhotos() { return _setPhotos; }

    public ImagePath get_headerPhoto() {
        return _headerPhoto;
    }

    public void set_headerPhoto(ImagePath headerPhoto) {
        _headerPhoto = headerPhoto;
    }

    public Location get_location() {
        return _location;
    }

    public void set_location(Location location) {
        _location = location;
    }

    public boolean is_bIsOnline() {
        return _bIsOnline;
    }

    public void set_tCelebrationHour(LocalTime tCelebrationHour) {
        _tCelebrationHour = tCelebrationHour;
    }

    public Set<Interest> get_setInterests() {
        return _setInterests;
    }

    public void set_setInterests(Set<Interest> setInterests) {
        _setInterests = setInterests;
    }

    public void set_bIsOnline(boolean bIsOnline) {
        _bIsOnline = bIsOnline;
    }

    public LocalDateTime get_tDeleteDate() { return _tDeleteDate; }

    public void set_tDeleteDate(LocalDateTime tDeleteDate) { _tDeleteDate = tDeleteDate; }
}
