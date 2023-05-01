package es.uca.tfg.backend.entity.rest;

import java.util.Date;

public class MapUserRegister {
    private String _sEmail;
    private String _sName;
    private String _sUsername;
    private String _sPassword;
    private Date _tBirthDate;
    private String _sMessage;

    public MapUserRegister(String sEmail, String sName, String sUsername, String sPassword, Date tBirthDate) {
        _sEmail = sEmail;
        _sName = sName;
        _sUsername = sUsername;
        _sPassword = sPassword;
        _tBirthDate = tBirthDate;
        _sMessage = "";
    }

    public String get_sEmail() {
        return _sEmail;
    }

    public String get_sName() {
        return _sName;
    }

    public String get_sUsername() {
        return _sUsername;
    }

    public String get_sPassword() {
        return _sPassword;
    }

    public Date get_tBirthDate() {
        return _tBirthDate;
    }

    public void set_sMessage(String _sMessage) {
        this._sMessage = _sMessage;
    }
}