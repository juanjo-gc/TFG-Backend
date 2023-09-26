package es.uca.tfg.backend.rest;

public class TicketDTO {

    private String _sSubject;
    private String _sDescription;

    private int _iIssuerId;
    private int _iReportedId;
    private int _iEventId;
    private int _iPostId;
    private String _sCategory;

    public TicketDTO(String sSubject, String sDescription, int iIssuerId, int iReportedId, int iEventId, int iPostId, String sCategory) {
        _sSubject = sSubject;
        _sDescription = sDescription;
        _iIssuerId = iIssuerId;
        _iReportedId = iReportedId;
        _iEventId = iEventId;
        _iPostId = iPostId;
        _sCategory = sCategory;
    }

    public String get_sSubject() {
        return _sSubject;
    }

    public String get_sDescription() {
        return _sDescription;
    }

    public int get_iIssuerId() {
        return _iIssuerId;
    }

    public int get_iReportedId() {
        return _iReportedId;
    }

    public int get_iEventId() {
        return _iEventId;
    }

    public int get_iPostId() {
        return _iPostId;
    }

    public String get_sCategory() {
        return _sCategory;
    }
}
