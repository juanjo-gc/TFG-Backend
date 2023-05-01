package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.DigestUtils;


@Entity
@DiscriminatorColumn(name = "role")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int _iId;
    @NotNull
    @Column(name = "email")
    private String _sEmail;
    @NotNull
    @Column(name = "password")
    private String _sPassword;
    @NotNull
    @Column(name = "passwordSalt")
    private String _sPasswordSalt;
    @NotNull
    @Column(name = "username")
    private String _sUsername;
    @NotNull
    @Column(insertable = false, updatable = false, name = "role")
    private String _sRole;

    public Person() {}
    public Person(String sEmail, String sPassword, String sUsername, String sRole) {
        _sEmail = sEmail;
        _sPasswordSalt = RandomStringUtils.randomAlphanumeric(32);
        _sPassword = DigestUtils.md5DigestAsHex((sPassword + _sPasswordSalt).getBytes());
        _sUsername = sUsername;
        _sRole = sRole;

    }

    public int get_iId() {
        return _iId;
    }

    public String get_sEmail() {
        return _sEmail;
    }

    public String get_sPassword() {
        return _sPassword;
    }

    public String get_sPasswordSalt() {
        return _sPasswordSalt;
    }

    public String get_sUsername() {
        return _sUsername;
    }

    public void set_sEmail(String _sEmail) {
        this._sEmail = _sEmail;
    }

    public void set_sPassword(String _sPassword) {
        this._sPassword = _sPassword;
    }

    public void set_sUsername(String _sUsername) {
        this._sUsername = _sUsername;
    }

    public String get_sRole()  { return _sRole; }

    public boolean checkPassword(String sPassword) {
        return DigestUtils.md5DigestAsHex((sPassword + _sPasswordSalt).getBytes()).equals(_sPassword);
    }
}
