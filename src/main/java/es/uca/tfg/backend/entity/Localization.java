package es.uca.tfg.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Columns;

@Entity
public class Localization {

    @Id
    @GeneratedValue
    private int _iId;

    @NotNull
    @Column(name = "name")
    private String _sName;

    @NotNull
    @Column(name = "latitude")
    private double _dLatitude;

    @NotNull
    @Column(name = "longitude")
    private double _dLongitude;

    public int get_iId() {
        return _iId;
    }

    public String get_sName() {
        return _sName;
    }

    public void set_sName(String sName) {
        _sName = sName;
    }

    public double get_dLatitude() {
        return _dLatitude;
    }

    public void set_dLatitude(double dLatitude) {
        _dLatitude = dLatitude;
    }

    public double get_dLongitude() {
        return _dLongitude;
    }

    public void set_dLongitude(double dLongitude) {
        _dLongitude = dLongitude;
    }
}
