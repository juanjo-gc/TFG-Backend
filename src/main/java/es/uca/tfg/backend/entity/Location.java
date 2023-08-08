package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Location {

    @Id
    @GeneratedValue
    private int _iId;

    @NotNull
    @Column(name = "name")
    private String _sName;

    @Column(name = "latitude")
    private float _fLatitude;

    @Column(name = "longitude")
    private float _fLongitude;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province _province;

    public Location() {}

    public Location(String sName, double dLatitude, double dLongitude, Province province) {
        _sName = sName;
        _fLatitude = (float) (dLatitude * 1000000) / 1000000;
        _fLongitude = (float) (dLongitude * 1000000) / 1000000;
        _province = province;
    }

    public int get_iId() {
        return _iId;
    }

    public String get_sName() {
        return _sName;
    }

    public void set_sName(String sName) {
        _sName = sName;
    }

    public double get_fLatitude() {
        return _fLatitude;
    }

    public void set_fLatitude(float dLatitude) {
        _fLatitude = dLatitude;
    }

    public float get_fLongitude() {
        return _fLongitude;
    }

    public void set_fLongitude(float dLongitude) {
        _fLongitude = dLongitude;
    }

    public Province get_province() { return _province; }

    public void set_province(Province province) { _province = province; }
}
