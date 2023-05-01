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

    @OneToMany
    @Column(name = "images")
    private Set<UserImage> _setUserImages;

    public User() {
        _setInterests = Collections.emptySet();
        _setUserImages = Collections.emptySet();
    }

    public User(String sEmail, String sPassword, String sUsername, String sRole, String sName, Date tBirthDate) {
        super(sEmail, sPassword, sUsername, sRole);
        _sName = sName;
        _tBirthDate = tBirthDate;
        _bIsPrivate = false;
        _setInterests = Collections.emptySet();
        _setUserImages = Collections.emptySet();
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

    public Set<UserImage> get_setUserImages() {
        return _setUserImages;
    }

    public void set_setUserImages(Set<UserImage> _setUserImages) {
        this._setUserImages = _setUserImages;
    }


    public UserImage getProfileImage() {
        String sImageName = "";
        UserImage userImage = null;
        for(UserImage i: _setUserImages) {
            if(i.is_bIsProfile())
                userImage = i;
        }
        return userImage != null ? userImage : new UserImage();
    }


}
