package es.uca.tfg.backend.rest;

public class MessageDTO {
    private int _iIssuerId;

    private int _iRecipientId;

    private String _sText;

    public MessageDTO(int iIssuerId, int iRecipientId, String sText) {
        _iIssuerId = iIssuerId;
        _iRecipientId = iRecipientId;
        _sText = sText;
    }


    public int get_iIssuerId() {
        return _iIssuerId;
    }

    public int get_iRecipientId() {
        return _iRecipientId;
    }

    public String get_sText() {
        return _sText;
    }
}
