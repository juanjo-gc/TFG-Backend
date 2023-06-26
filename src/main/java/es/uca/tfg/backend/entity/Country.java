package es.uca.tfg.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Entity
public class Country {
    @Id
    @GeneratedValue
    private int _iId;

    @NotNull
    private String _sName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "_country")
    @JsonIgnore
    private Set<Region> _setRegions;

    public Country() {}

    public Country(String sName) {
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

    public Set<Region> get_setRegions() {
        return _setRegions;
    }

    public void set_setRegions(Set<Region> _setRegions) {
        this._setRegions = _setRegions;
    }
}
