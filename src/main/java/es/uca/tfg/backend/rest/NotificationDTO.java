package es.uca.tfg.backend.rest;



public class NotificationDTO {
    
    private int _iId;

    private String _sInfo;

    private int _iRecipientId;

    private int _iIssuerId;

    private int _iPostId;

    private int _iEventId;

    private String _sType;

    //public NotificationDTO() {}

    public NotificationDTO(String sInfo, int iRecipientId, int iIssuerId, int iPostId, int iEventId, String sType) {
        _sInfo = sInfo;
        _iRecipientId = iRecipientId;
        _iIssuerId = iIssuerId;
        _iPostId = iPostId;
        _iEventId = iEventId;
        _sType = sType;
    }


    /*

    public NotificationDTO(String sInfo, int iRecipientId, String sType, int iIssuerId) {
        _sInfo = sInfo;
        _iRecipientId = iRecipientId;
        _sType = sType;
        _iIssuerId = iIssuerId;
        _iEventId = 0;
        _iPostId = 0;
    }

    public NotificationDTO(String sInfo, int iRecipientId, String sType, int iIssuerId, int iEventId) {
        _sInfo = sInfo;
        _iRecipientId = iRecipientId;
        _sType = sType;
        _iIssuerId = iIssuerId;
        _iEventId = iEventId;
        _iPostId = 0;
    }
    public NotificationDTO(String sInfo, int iRecipientId, String sType) {
        _sInfo = sInfo;
        _iRecipientId = iRecipientId;
        _sType = sType;
        _iIssuerId = 0;
        _iEventId = 0;
        _iPostId = 0;
    }
    public NotificationDTO(String sInfo, int iRecipientId, int iIssuerId, int iPostId, String sType) {
        _sInfo = sInfo;
        _iRecipientId = iRecipientId;
        _sType = sType;
        _iIssuerId = iIssuerId;
        _iEventId = 0;
        _iPostId = iPostId;
    }

     */

    public int get_iId() {
        return _iId;
    }

    public String get_sInfo() {
        return _sInfo;
    }
    public int get_iRecipientId() {
        return _iRecipientId;
    }

    public int get_iIssuerId() {
        return _iIssuerId;
    }

    public int get_iPostId() {
        return _iPostId;
    }

    public int get_iEventId() {
        return _iEventId;
    }

    public String get_sType() {
        return _sType;
    }
}
