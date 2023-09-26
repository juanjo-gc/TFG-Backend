package es.uca.tfg.backend.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryDTO {

    private String _sName;
    private String _sDisplayName;

    public CategoryDTO(@JsonProperty("sName") String sName, @JsonProperty("sDisplayName") String sDisplayName) { _sName = sName; _sDisplayName = sDisplayName; }

    public String get_sName() { return _sName; }

    public String get_sDisplayName() { return _sDisplayName; }
}
