package es.uca.tfg.backend.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;


public class EventDTO {

    private String _sTitle;

    private LocalDate _tCelebratedAt;

    private LocalTime _tCelebrationHour;

    private String _sDescription;

    private int _iOrganizerId;

    private Set<String> _setInterests;

    private String _sLocationName;

    private float _dLatitude;

    private float _dLongitude;

    private String _sProvinceName;
    private boolean _bIsOnline;

    public EventDTO() {}

    public EventDTO(@JsonProperty("sTitle") String sTitle, @JsonProperty("tCelebratedAt") LocalDate tCelebratedAt, @JsonProperty("tCelebrationHour") LocalTime tCelebrationHour,
                    @JsonProperty("sDescription") String sDescription, @JsonProperty("iOrganizerId") Integer iOrganizerId, @JsonProperty("setInterests") Set<String> setInterests,
                    @JsonProperty("sLocationName") String sLocationName, @JsonProperty("dLatitude") float dLatitude, @JsonProperty("dLongitude") float dLongitude,
                    @JsonProperty("sProvinceName") String sProvinceName, @JsonProperty("bIsOnline") boolean bIsOnline) {
        _sTitle = sTitle;
        _tCelebratedAt = tCelebratedAt;
        _tCelebrationHour = tCelebrationHour;
        _sDescription = sDescription;
        _iOrganizerId = iOrganizerId;
        _setInterests = setInterests;
        _sLocationName = sLocationName;
        _dLatitude = dLatitude;
        _dLongitude = dLongitude;
        _sProvinceName = sProvinceName;
        _bIsOnline = bIsOnline;
    }

    /*
    public EventDTO(String sTitle, LocalDate tCelebratedAt, LocalTime tCelebrationHour, String sDescription, Integer iOrganizerId, Set<String> setInterests, boolean bIsOnline) {
        _sTitle = sTitle;
        _tCelebratedAt = tCelebratedAt;
        _tCelebrationHour = tCelebrationHour;
        _sDescription = sDescription;
        _iOrganizerId = iOrganizerId;
        _setInterests = setInterests;
        _sLocationName = null;
        _dLatitude = 0;
        _dLongitude = 0;
        _sProvinceName = null;
        _bIsOnline = bIsOnline;
    }

     */

    public String get_sTitle() {
        return _sTitle;
    }

    public LocalDate get_tCelebratedAt() {
        return _tCelebratedAt;
    }

    public LocalTime get_tCelebrationHour() { return _tCelebrationHour; }

    public String get_sDescription() {
        return _sDescription;
    }

    public int get_iOrganizerId() {
        return _iOrganizerId;
    }

    public Set<String> get_setInterests() {
        return _setInterests;
    }

    public String get_sLocationName() {
        return _sLocationName;
    }

    public float get_dLatitude() {
        return _dLatitude;
    }

    public float get_dLongitude() {
        return _dLongitude;
    }

    public String get_sProvinceName() { return _sProvinceName; }

    public boolean is_bIsOnline() { return _bIsOnline; }
}
