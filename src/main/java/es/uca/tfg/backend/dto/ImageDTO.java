package es.uca.tfg.backend.dto;

import org.springframework.web.multipart.MultipartFile;

public class ImageDTO {
    private MultipartFile _multipartFile;

    public ImageDTO() {}

    public ImageDTO(MultipartFile multipartFile) {
        _multipartFile = multipartFile;
    }

    public MultipartFile get_multipartFile() {
        return _multipartFile;
    }

    public void set_multipartFile(MultipartFile _multipartFile) {
        this._multipartFile = _multipartFile;
    }
}
