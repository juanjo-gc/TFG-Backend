package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Operation {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int _iId;

    @NotNull
    @Column(name = "information")
    private String _sInformation;

    @NotNull
    @Column(name = "timestamp")
    private LocalDateTime _tTimestamp;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin _admin;

    public Operation() {}

    public Operation(String sInformation, Admin admin) {
        _tTimestamp = LocalDateTime.now();
        _admin = admin;
        _sInformation = sInformation;
    }

    public int get_iId() {
        return _iId;
    }

    public String get_sInformation() {
        return _sInformation;
    }

    public LocalDateTime get_tTimestamp() {
        return _tTimestamp;
    }

    public Admin get_admin() {
        return _admin;
    }
}
