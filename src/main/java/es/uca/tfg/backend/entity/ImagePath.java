package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.awt.*;

@Entity
@DiscriminatorColumn(name = "type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ImagePath {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int _iId;

    @NotNull
    @Column(insertable = false, updatable = false, name = "type")
    private String _sType;

    @NotNull
    @Column(name = "name")
    private String _sName;

    public ImagePath() {}
    public ImagePath(String sName, String sType) {
        _sName = sName;
        _sType = sType;
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

    public String get_sType() {
        return _sType;
    }

    public void set_sType(String sType) {
        _sType = sType;
    }
}
