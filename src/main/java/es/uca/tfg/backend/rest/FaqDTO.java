package es.uca.tfg.backend.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FaqDTO {

    private String _sQuestion;

    private String _sAnswer;



    public FaqDTO(@JsonProperty("sQuestion") String sQuestion, @JsonProperty("sAnswer") String sAnswer) {
        _sQuestion = sQuestion;
        _sAnswer = sAnswer;
    }

    public String get_sQuestion() {
        return _sQuestion;
    }

    public String get_sAnswer() {
        return _sAnswer;
    }
}
