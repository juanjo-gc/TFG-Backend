package es.uca.tfg.backend.rest;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class UserFilterDTO {

    private List<String> _asInterests;

    private String _sCountry;

    private String _sRegion;

    private String _sProvince;

    public UserFilterDTO() {}

    @JsonCreator
    public UserFilterDTO(List<String> asInterests, String sCountry, String sRegion, String sProvince) {
        _asInterests = asInterests;
        _sCountry = sCountry;
        _sRegion = sRegion;
        _sProvince = sProvince;
    }

    public List<String> get_asInterests() {
        return _asInterests;
    }



    public String get_sCountry() {
        return _sCountry;
    }

    public String get_sRegion() {
        return _sRegion;
    }

    public String get_sProvince() {
        return _sProvince;
    }

    public void set_sCountry(String _sCountry) {
        this._sCountry = _sCountry;
    }

    public void set_sRegion(String _sRegion) {
        this._sRegion = _sRegion;
    }

    public void set_sProvince(String _sProvince) {
        this._sProvince = _sProvince;
    }
}
