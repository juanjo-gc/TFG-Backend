package es.uca.tfg.backend.entity;

import jakarta.persistence.Entity;

@Entity
public class Admin extends Person {

    public Admin() {}
    public Admin(String sEmail, String sPassword, String sUsername, String sRole){
        super(sEmail, sPassword, sUsername, sRole);
    }
}
