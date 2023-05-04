package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Entity
public class Region {
    @Id
    @GeneratedValue
    private int _iId;

    @NotNull
    private String _sName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "_region")
    private Set<Province> _setProvinces;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = true)
    private Country _country;

    public Region(String sName) {
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

    public Set<Province> get_setProvinces() {
        return _setProvinces;
    }

    public void set_setProvinces(Set<Province> _setProvinces) {
        this._setProvinces = _setProvinces;
    }

    public Country get_country() {
        return _country;
    }

    public void set_country(Country _country) {
        this._country = _country;
    }
}
