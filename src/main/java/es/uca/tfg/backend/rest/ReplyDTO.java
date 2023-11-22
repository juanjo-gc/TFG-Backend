package es.uca.tfg.backend.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReplyDTO {

    private int _iPersonId;

    private String _sText;

    private int _iTicketId;

    public ReplyDTO(@JsonProperty("iPersonId") int iPersonId, @JsonProperty("sText") String sText, @JsonProperty("iTicketId") int iTicketId) {
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
