package es.uca.tfg.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Entity
public class User extends Person {
    @NotNull
    @Column(name = "name")
    private String _sName;
    @Column(name = "description")
    private String _sDescription;
    @NotNull
    @Column(name = "isPrivate")
    private Boolean _bIsPrivate;
    @NotNull
    @Column(name = "birthDate")
    private Date _tBirthDate;

    @Column(name = "suspended")
    boolean _bIsSuspended;


    @ManyToMany
    @JoinTable(name = "user_interests")
    @Column(name = "interests")
    private Set<Interest> _setInterests;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province _province;

    @OneToMany
    @Column(name = "images")
    private Set<ImagePath> _setImagePath;
    
    @OneToOne
    @JoinColumn(name = "profileImage")
    @JsonIgnore
    private ImagePath _profileImagePath;

    @ManyToMany
    @JoinTable(
            name = "user_following",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    @JsonIgnore
    private Set<User> _setFollowing;

    @ManyToMany(mappedBy = "_setFollowing")
    @JsonIgnore
    private Set<User> _setFollowers;

    @OneToMany(mappedBy = "_user")
    @JsonIgnore
    private Set<Post> _setPosts;

    @ManyToMany(mappedBy = "_setLikes")
    @JsonIgnore
    private Set<Post> _setLikedPosts;

    @OneToMany(mappedBy = "_issuer")
    @JsonIgnore
    private Set<Message> _setMessages;

    @OneToMany(mappedBy = "_recipient")
    @JsonIgnore
    private Set<Message> _setReceivedMessages;

    @ManyToMany
    @JoinTable(
            name = "user_blockedby",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "blockedby_id")
    )
    @JsonIgnore
    private Set<User> _setBlockedBy;


    public User() {
        _setInterests = Collections.emptySet();
        _setImagePath = Collections.emptySet();
        _profileImagePath = new ImagePath();
        _province = new Province();
        _bIsSuspended = false;
    }

    public User(String sEmail, String sPassword, String sUsername, String sDescription, String sRole, String sName, Date tBirthDate, Province province) {
        super(sEmail, sPassword, sUsername, sRole);
        _sName = sName;
        _sDescription = sDescription;
        _tBirthDate = tBirthDate;
        _bIsPrivate = false;
        _setInterests = Collections.emptySet();
        _setImagePath = Collections.emptySet();
        _bIsSuspended = false;
        _province = province;
    }

    public String get_sName() {
        return _sName;
    }

    public String get_sDescription() {
        return _sDescription;
    }

    public Boolean get_bIsPrivate() {
        return _bIsPrivate;
    }

    public Date get_tBirthDate() {
        return _tBirthDate;
    }

    public Set<Interest> get_setInterests() {
        return _setInterests;
    }

    public void set_sName(String _sName) {
        this._sName = _sName;
    }

    public void set_sDescription(String _sDescription) {
        this._sDescription = _sDescription;
    }

    public void set_bIsPrivate(Boolean _bIsPrivate) {
        this._bIsPrivate = _bIsPrivate;
    }

    public void set_tBirthDate(Date _tBirthDate) {
        this._tBirthDate = _tBirthDate;
    }

    public void set_setInterests(Set<Interest> _setInterests) {
        this._setInterests = _setInterests;
    }

    public void set_setUserImages(Set<ImagePath> setImagePath) {
        _setImagePath = setImagePath;
    }

    public Set<ImagePath> get_setImagePath() {
        return _setImagePath;
    }

    public void set_setImagePath(Set<ImagePath> _setImagePath) {
        this._setImagePath = _setImagePath;
    }

    public ImagePath get_profileImagePath() {
        return _profileImagePath;
    }

    public void set_profileImagePath(ImagePath _profileImagePath) {
        this._profileImagePath = _profileImagePath;
    }

    public Province get_province() {
        return _province;
    }

    public void set_province(Province province) {
        _province = province;
    }

    public Set<User> get_setFollowing() {
        return _setFollowing;
    }

    public void set_setFollowing(Set<User> _setFollowing) {
        this._setFollowing = _setFollowing;
    }


    public Set<User> get_setFollowers() {
        return _setFollowers;
    }

    public void set_setFollowers(Set<User> _setFollowers) {
        this._setFollowers = _setFollowers;
    }

    public Set<Post> get_setLikedPosts() {
        return _setLikedPosts;
    }

    public Set<Message> get_setMessages() {
        return _setMessages;
    }

    public Set<Message> get_setReceivedMessages() {
        return _setReceivedMessages;
    }

    public Set<Post> get_setPosts() { return _setPosts; }

    public void set_setPosts(Set<Post> _setPosts) { this._setPosts = _setPosts; }

    public boolean is_bIsSuspended() { return _bIsSuspended; }

    public void set_bIsSuspended(boolean bIsSuspended) { _bIsSuspended = bIsSuspended; }

    public Set<User> get_setBlockedBy() { return _setBlockedBy; }


}
