package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(indexes = { @Index(name = "UX_TypeNotification__name", columnList = "name") })
public class TypeNotification {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int _iId;

    @NotNull
    @Column(name = "name", unique = true)
    private String _sName;

    public TypeNotification() {}

    public TypeNotification(String sName) {
        _sName = sName;
    }

    public int get_iId() {
        return _iId;
    }

    public String get_sName() {
        return _sName;
    }
}
