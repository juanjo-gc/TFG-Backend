package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Province {
    @Id
    @GeneratedValue
    private int _iId;

    @NotNull
    private String _sName;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region _region;
}
