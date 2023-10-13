package es.uca.tfg.backend.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegionDTO {

    private String _sName;
    private String _sCountry;

    public RegionDTO(@JsonProperty("sName") String sName, @JsonProperty("sCountry") String sCountry) { _sName = sName; _sCountry = sCountry; }

    public String get_sName() { return _sName; }

    public String get_sCountry() { return _sCountry; }
}
