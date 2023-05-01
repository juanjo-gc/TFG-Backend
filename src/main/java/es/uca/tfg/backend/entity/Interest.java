package es.uca.tfg.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

@Entity
public class Interest {
    @Id
    @GeneratedValue
    private int _iId;

    @NotNull
    private String _sName;

    @ManyToMany(mappedBy = "_setInterests")
    private Set<User> _setUsers;

    public Interest() {}
    public Interest(String sName) {
        _sName = sName;
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
}
