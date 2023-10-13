package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(indexes = { @Index(name = "UX_Category__name", columnList = "name") })
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int _iId;

    @NotNull
    @Column(name = "name", unique = true)
    private String _sName;

    public Category() {}

    public Category(String sName) {
        _sName = sName;
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
}
