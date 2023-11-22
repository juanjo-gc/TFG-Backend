package es.uca.tfg.backend.rest;

public class AboutMeQuestionAnswerDTO {
    
    private String _sQuestion;
    
    private String _sAnswer;
    
    private int _iAdminId;
    
    private int _iUserId;
    
    private int _iQuestionId;
    
    private int _iAnswerId;

    public AboutMeQuestionAnswerDTO(String sQuestion, String sAnswer, int iAdminId, int iUserId, int iQuestionId, int iAnswerId) {
        _sQuestion = sQuestion;
        _sAnswer = sAnswer;
        _iAdminId = iAdminId;
        _iUserId = iUserId;
        _iQuestionId = iQuestionId;
        _iAnswerId = iAnswerId;
    }

    public String get_sQuestion() {
        return _sQuestion;
    }

    public String get_sAnswer() {
        return _sAnswer;
    }

    public int get_iAdminId() {
        return _iAdminId;
    }

    public int get_iUserId() {
        return _iUserId;
    }

    public int get_iQuestionId() {
        return _iQuestionId;
    }

    public int get_iAnswerId() {
        return _iAnswerId;
    }
}
