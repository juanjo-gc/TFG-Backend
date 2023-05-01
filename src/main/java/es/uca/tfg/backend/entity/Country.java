package es.uca.tfg.backend.entity;

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
    private Set<Region> _setRegions;

}
