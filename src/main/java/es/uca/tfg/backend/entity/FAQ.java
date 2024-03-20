package es.uca.tfg.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class FAQ {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int _iId;

    @NotNull
    @Column(name = "question")
    private String _sQuestion;

    @NotNull
    @Column(name = "answer", length = 1024)
    private String _sAnswer;

    public FAQ() {}

    public FAQ(String sQuestion, String sAnswer) {
        _sQuestion = sQuestion;
        _sAnswer = sAnswer;
    }

    public int get_iId() {
        return _iId;
    }

    public String get_sQuestion() {
        return _sQuestion;
    }

    public void set_sQuestion(String sQuestion) {
        _sQuestion = sQuestion;
    }

    public String get_sAnswer() {
        return _sAnswer;
    }

    public void set_sAnswer(String sAnswer) {
        _sAnswer = sAnswer;
    }
}
