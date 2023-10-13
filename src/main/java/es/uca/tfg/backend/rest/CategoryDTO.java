package es.uca.tfg.backend.rest;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CategoryDTO {

    private String _sName;

    public CategoryDTO(@JsonProperty("sName")String sName) { _sName = sName; }
    public String get_sName() { return _sName; }

}
