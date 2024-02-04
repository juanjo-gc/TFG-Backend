package es.uca.tfg.backend.rest;

import java.util.Date;

public class RegisterDTO {
    private String _sEmail;
    private String _sName;
    private String _sUsername;
    private String _sPassword;
    private Date _tBirthDate;
    private String _sMessage;
    private String _sProvince;

    public RegisterDTO(String sEmail, String sName, String sUsername, String sPassword, Date tBirthDate, String sProvince) {
        _sEmail = sEmail;
        _sName = sName;
        _sUsername = sUsername;
        _sPassword = sPassword;
        _tBirthDate = tBirthDate;
        _sMessage = "";
        _sProvince = sProvince;
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

    public String get_sProvince() { return _sProvince; }
}