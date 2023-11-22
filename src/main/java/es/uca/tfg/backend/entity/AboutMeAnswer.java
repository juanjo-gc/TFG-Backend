package es.uca.tfg.backend.entity;

import jakarta.persistence.*;

@Entity
public class AboutMeAnswer {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int _iId;

    @Column(name = "answer")
    private String _sAnswer;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private AboutMeQuestion _question;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User _user;

    public AboutMeAnswer() {}

    public AboutMeAnswer(String sAnswer, AboutMeQuestion question, User user) {
        _sAnswer = sAnswer;
        _question = question;
        _user = user;
    }

    public int get_iId() {
        return _iId;
    }


    public String get_sAnswer() { return _sAnswer; }

    public void set_sAnswer(String sAnswer) { _sAnswer = sAnswer; }

    public AboutMeQuestion get_question() { return _question; }

    public void set_question(AboutMeQuestion question) { _question = question; }

    public User get_user() { return _user; }

}
