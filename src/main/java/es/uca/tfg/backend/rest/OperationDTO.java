package es.uca.tfg.backend.rest;

public class OperationDTO {

    private String _sInformation;

    private int _iAdminId;

    public OperationDTO(String sInformation, int iAdminId) { _sInformation = sInformation; _iAdminId = iAdminId; }

    public String get_sInformation() { return _sInformation; }

    public int get_iAdminId() { return _iAdminId; }
}
