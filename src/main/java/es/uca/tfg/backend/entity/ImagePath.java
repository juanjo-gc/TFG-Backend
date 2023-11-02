package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import net.minidev.json.annotate.JsonIgnore;

import java.awt.*;
import java.time.LocalDateTime;

@Entity
public class ImagePath {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int _iId;

    @NotNull
    @Column(name = "name")
    private String _sName;

    @Column(name = "deleteDate")
    private LocalDateTime _tDeleteDate;

    @OneToOne(mappedBy = "_profileImagePath")
    @JsonIgnore
    private User _user;

    public ImagePath() {}
    public ImagePath(String sName) { _sName = sName; _tDeleteDate = null; }

    public int get_iId() {
        return _iId;
    }

    public String get_sName() {
        return _sName;
    }

    public void set_sName(String _sName) {
        this._sName = _sName;
    }

    public LocalDateTime get_tDeleteDate() {
        return _tDeleteDate;
    }

    public void set_tDeleteDate(LocalDateTime tDeleteDate) {
        _tDeleteDate = tDeleteDate;
    }



    public User get_user() {
        return _user;
    }

    public void set_user(User _user) {
        this._user = _user;
    }
}
