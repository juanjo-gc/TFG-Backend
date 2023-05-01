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
    @JoinColumn(name = "country_id", nullable = false)
    private Country _country;
}
