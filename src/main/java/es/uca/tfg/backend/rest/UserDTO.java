package es.uca.tfg.backend.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserDTO {
    private int _iUserId;
    private String _sName;
    private String _sUsername;
    private String _sEmail;
    private String _sDescription;
    private int _iProvinceId;
    private String _sCurrentPassword;
    private String _sNewPasswordConfirmation;
    private String _sNewPassword;
    private boolean _bIsPrivate;
    private List<String> _asInterests;

    public UserDTO(@JsonProperty("iUserId") int iUserId, @JsonProperty("sName") String sName, @JsonProperty("sUsername") String sUsername, @JsonProperty("sEmail") String sEmail, @JsonProperty("sDescription") String sDescription,
                   @JsonProperty("iProvinceId") int iProvinceId, @JsonProperty("sCurrentPassword") String sCurrentPassword, @JsonProperty("sNewPasswordConfirmation") String sNewPasswordConfirmation,
                   @JsonProperty("sNewPassword") String sNewPassword, @JsonProperty("bIsPrivate") boolean bIsPrivate, @JsonProperty("asInterests") List<String> asInterests) {
        _iUserId = iUserId;
        _sName = sName;
        _sUsername = sUsername;
        _sEmail = sEmail;
        _sDescription = sDescription;
        _iProvinceId = iProvinceId;
        _sCurrentPassword = sCurrentPassword;
        _sNewPasswordConfirmation = sNewPasswordConfirmation;
        _sNewPassword = sNewPassword;
        _bIsPrivate = bIsPrivate;
        _asInterests = asInterests;
    }

    public int get_iUserId() {
        return _iUserId;
    }

    public String get_sName() {
        return _sName;
    }

    public String get_sUsername() {
        return _sUsername;
    }

    public String get_sEmail() {
        return _sEmail;
    }

    public int get_iProvinceId() {
        return _iProvinceId;
    }

    public String get_sDescription() {
        return _sDescription;
    }

    public String get_sCurrentPassword() {
        return _sCurrentPassword;
    }

    public String get_sNewPasswordConfirmation() {
        return _sNewPasswordConfirmation;
    }

    public String get_sNewPassword() {
        return _sNewPassword;
    }

    public boolean is_bIsPrivate() {
        return _bIsPrivate;
    }

    public List<String> get_asInterests() {
        return _asInterests;
    }
}
