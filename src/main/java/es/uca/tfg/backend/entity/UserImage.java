package es.uca.tfg.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class UserImage extends ImagePath {

    @NotNull
    @Column(name = "isProfile")
    private boolean _bIsProfile;

    public UserImage() {
        super();
    }

    public UserImage(String sName, String sType, boolean bIsProfile) {
        super(sName, sType);
        _bIsProfile = bIsProfile;
    }

    public boolean is_bIsProfile() {
        return _bIsProfile;
    }

    public void set_bIsProfile(boolean _bIsProfile) {
        this._bIsProfile = _bIsProfile;
    }
}
