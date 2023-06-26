package es.uca.tfg.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Entity
public class Province {
    @Id
    @GeneratedValue
    private int _iId;

    @NotNull
    private String _sName;

    @OneToMany(mappedBy = "_province")
    @JsonIgnore
    private Set<User> _setUsers;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = true)
    private Region _region;

    public Province() {}

    public Province(String sName, Region region) {
        _sName = sName;
        _region = region;
    }

    public int get_iId() {
        return _iId;
    }

    public String get_sName() {
        return _sName;
    }

    public void set_sName(String _sName) {
        this._sName = _sName;
    }

    public Set<User> get_setUsers() {
        return _setUsers;
    }

    public void set_setUsers(Set<User> _setUsers) {
        this._setUsers = _setUsers;
    }

    public Region get_region() {
        return _region;
    }

    public void set_region(Region _region) {
        this._region = _region;
    }
}
