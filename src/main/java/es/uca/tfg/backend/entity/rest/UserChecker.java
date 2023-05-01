package es.uca.tfg.backend.entity.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserChecker {

    public static class UsernameChecker {
        private String _sUsername;
        private boolean _bIsUsernameTaken;

        public UsernameChecker(@JsonProperty("sUsername") String sUsername) {
            _sUsername = sUsername;
        }

        public String get_sUsername() {
            return _sUsername;
        }

        public boolean is_bIsUsernameTaken() {
            return _bIsUsernameTaken;
        }

        public void set_bIsUsernameTaken(boolean _bIsUsernameTaken) {
            this._bIsUsernameTaken = _bIsUsernameTaken;
        }
    }

    public static class EmailChecker {
        private String _sEmail;
        private boolean _bIsEmailTaken;

        public EmailChecker(@JsonProperty("sEmail") String sEmail) {
            _sEmail = sEmail;
        }
        public String get_sEmail() {
            return _sEmail;
        }
        public boolean is_bIsEmailTaken() {
            return _bIsEmailTaken;
        }
        public void set_bIsEmailTaken(boolean _bIsEmailTaken) {
            this._bIsEmailTaken = _bIsEmailTaken;
        }
    }

    public static class LoginChecker {
        private String _sEmail;
        private String _sPassword;

        public LoginChecker(@JsonProperty("sEmail") String sEmail, @JsonProperty("sPassword") String sPassword) {
            _sEmail = sEmail;
            _sPassword = sPassword;
        }

        public String get_sEmail() {
            return _sEmail;
        }

        public String get_sPassword() {
            return _sPassword;
        }
    }



}
