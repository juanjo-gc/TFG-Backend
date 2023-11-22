package es.uca.tfg.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;


@Entity
public class AboutMeQuestion {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int _iId;

    @NotNull
    @Column(name = "question")
    private String _sQuestion;

    public AboutMeQuestion() {}

    public AboutMeQuestion(String sQuestion) { _sQuestion = sQuestion; }

    public int get_iId() { return _iId; }

    public String get_sQuestion() { return _sQuestion; }

    public void set_sQuestion(String sQuestion) { _sQuestion = sQuestion; }
}
