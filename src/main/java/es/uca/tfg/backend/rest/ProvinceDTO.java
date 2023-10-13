package es.uca.tfg.backend.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProvinceDTO {

    private String _sName;
    private String _sRegion;

    public ProvinceDTO(@JsonProperty("sName") String sName, @JsonProperty("sRegion") String sRegion) { _sName = sName; _sRegion = sRegion; }

    public String get_sName() { return _sName; }

    public String get_sRegion() { return _sRegion; }
}
