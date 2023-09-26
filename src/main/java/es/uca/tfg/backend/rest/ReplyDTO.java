package es.uca.tfg.backend.rest;

public class ReplyDTO {

    private int _iPersonId;

    private String _sText;

    private int _iTicketId;

    public ReplyDTO(int iPersonId, String sText, int iTicketId) {
        _iPersonId = iPersonId;
        _sText = sText;
        _iTicketId = iTicketId;
    }

    public int get_iPersonId() {
        return _iPersonId;
    }

    public String get_sText() {
        return _sText;
    }

    public int get_iTicketId() {
        return _iTicketId;
    }
}
