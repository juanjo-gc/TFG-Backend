package es.uca.tfg.backend.rest;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class EventFilterDTO {

    private boolean _bIsOnline;
    private List<String> _asInterests;

    private String _sCountry;

    private String _sRegion;

    private String _sProvince;

    public EventFilterDTO() {}

    @JsonCreator
    public EventFilterDTO(boolean bIsOnline, List<String> asInterests, String sCountry, String sRegion, String sProvince) {
        _bIsOnline = bIsOnline;
        _asInterests = asInterests;
        _sCountry = sCountry;
        _sRegion = sRegion;
        _sProvince = sProvince;
    }

    public boolean get_bIsOnline() {
        return _bIsOnline;
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

}
