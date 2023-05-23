package es.uca.tfg.backend.rest;

public class PostDTO {
    private String _sText;
    private int _iUserId;

    public PostDTO(String sText, int iUserId) {
        _sText = sText;
        _iUserId = iUserId;
    }

    public String get_sText() {
        return _sText;
    }

    public void set_sText(String _sText) {
        this._sText = _sText;
    }

    public int get_iUserId() {
        return _iUserId;
    }

    public void set_iUserId(int _iUserId) {
        this._iUserId = _iUserId;
    }
}
