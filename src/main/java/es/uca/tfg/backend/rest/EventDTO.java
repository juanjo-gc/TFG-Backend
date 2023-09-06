package es.uca.tfg.backend.rest;

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

    public EventDTO(String sTitle, LocalDate tCelebratedAt, LocalTime tCelebrationHour, String sDescription, Integer iOrganizerId, Set<String> setInterests, String sLocationName,
                    float dLatitude, float dLongitude, String sProvinceName, boolean bIsOnline) {
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
