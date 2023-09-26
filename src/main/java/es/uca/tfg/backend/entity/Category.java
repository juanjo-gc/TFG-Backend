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
    @Column(name = "name")
    private String _sName;

    @NotNull
    @Column(name = "displayName")
    private String _sDisplayName;

    public Category() {}

    public Category(String sName, String sDisplayName) {
        _sName = sName;
        _sDisplayName = sDisplayName;
    }

    public int get_iId() {
        return _iId;
    }

    public String get_sName() {
        return _sName;
    }

    public String get_sDisplayName() { return _sDisplayName; }
}
