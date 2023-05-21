package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.mapping.Array;


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

    @ManyToMany
    @JoinTable(name = "User_Interests")
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
    private ImagePath _profileImagePath;

    @ManyToMany
    @JoinTable(
            name = "user_following",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    private Set<User> _setFollowing;

    @ManyToMany(mappedBy = "following")
    private Set<User> _setFollowers;

    @OneToMany
    @Column(name = "posts")
    private Set<Post> _setPosts;

    public User() {
        _setInterests = Collections.emptySet();
        _setImagePath = Collections.emptySet();
        _profileImagePath = new ImagePath();
        _province = new Province();
    }

    public User(String sEmail, String sPassword, String sUsername, String sRole, String sName, Date tBirthDate) {
        super(sEmail, sPassword, sUsername, sRole);
        _sName = sName;
        _tBirthDate = tBirthDate;
        _bIsPrivate = false;
        _setInterests = Collections.emptySet();
        _setImagePath = Collections.emptySet();
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
}
