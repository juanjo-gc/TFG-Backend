package es.uca.tfg.backend.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InterestDTO {

    private String _sName;

    public InterestDTO(@JsonProperty("sName") String sName) { _sName = sName; }

    public String get_sName() { return _sName; }
}
