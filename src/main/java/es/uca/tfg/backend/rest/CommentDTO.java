package es.uca.tfg.backend.rest;

public class CommentDTO {

    private int _iUserId;
    private int _iEventId;
    private String _sText;

    public CommentDTO(int iUserId, int iEventId, String sText) {
        _iUserId = iUserId;
        _iEventId = iEventId;
        _sText = sText;
    }

    public int get_iUserId() {
        return _iUserId;
    }

    public int get_iEventId() {
        return _iEventId;
    }

    public String get_sText() {
        return _sText;
    }
}
