package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.Event;
import es.uca.tfg.backend.entity.ImagePath;
import es.uca.tfg.backend.entity.User;
import es.uca.tfg.backend.repository.EventRepository;
import es.uca.tfg.backend.repository.ImagePathRepository;
import es.uca.tfg.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class ImagePathController {

    @Autowired
    private ImagePathRepository _imagePathRepository;
    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private EventRepository _eventRepository;

    private String _sEventsUploadPath = new FileSystemResource("").getFile().getAbsolutePath() + "\\src\\main\\resources\\static\\images\\events\\";



    @PostMapping("/uploadEventHeaderImage")
    public boolean saveProfileImage(@RequestParam("id") int iId, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        Event event = _eventRepository.findById(iId).get();
        System.out.println(_sEventsUploadPath);
        Path path = Paths.get(_sEventsUploadPath);

        if(event.get_headerPhoto() != null) {
            File file = new File(_sEventsUploadPath + event.get_headerPhoto().get_sName());
            file.delete();
        }

        String sFilename = event.get_iId() + "-" + multipartFile.getOriginalFilename();
        File file = new File(_sEventsUploadPath + sFilename);
        System.out.println("Guardando la imagen con nombre: " + sFilename);
        System.out.println("Ruta: " + path.toString());
        multipartFile.transferTo(file);

        ImagePath imagePath = new ImagePath(sFilename);
        imagePath = _imagePathRepository.save(imagePath);
        event.set_headerPhoto(imagePath);
        event = _eventRepository.save(event);
        return event.get_headerPhoto() != null ? true : false;
    }

    @PostMapping("/uploadEventImages")
    public String saveEventImages(@RequestParam("id") int iId, @RequestParam("file[]") MultipartFile[] aMultipartFiles) throws IOException {
        Event event = _eventRepository.findById(iId).get();
        System.out.println(_sEventsUploadPath);

        for(int i = 0; i < aMultipartFiles.length; i++) {
            String sFilename = event.get_iId() + "-" + aMultipartFiles[i].getOriginalFilename();
            File file = new File(_sEventsUploadPath + sFilename);
            aMultipartFiles[i].transferTo(file);
            System.out.println("Guardada la imagen " + sFilename + " en la ruta " + _sEventsUploadPath);
            ImagePath imagePath = new ImagePath(sFilename);
            imagePath = _imagePathRepository.save(imagePath);
            event.get_setPhotos().add(imagePath);
            event = _eventRepository.save(event);
        }
        return "Imágenes subidas correctamente";
    }

    @GetMapping("/getEventHeaderImage/{id}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getHeaderImage(@PathVariable("id") int iId) throws FileNotFoundException {
        Event event = _eventRepository.findById(iId).get();
        if(event.get_headerPhoto() != null) {
            String sImageName;
            sImageName = event.get_headerPhoto().get_sName();
            System.out.print(sImageName);
            File file = new File(_sEventsUploadPath + sImageName);
            FileInputStream fileInputStream = new FileInputStream(file);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(fileInputStream));
        } else {    // Si no hay foto, devuelve nulo
            return ResponseEntity.ok()
                    .body(null);
        }
    }

    @GetMapping("/getEventPhotos/{eventId}")
    public Set<ImagePath> getEventPhotos(@PathVariable("eventId") int iEventId) {
        Optional<Event> optionalEvent = _eventRepository.findById(iEventId);
        return optionalEvent.isPresent() ? optionalEvent.get().get_setPhotos() : Collections.emptySet();
    }

    @GetMapping("/getEventImage/{imageName}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getImagePath(@PathVariable("imageName") String sImageName) throws FileNotFoundException {
        File file = new File(_sEventsUploadPath + sImageName);
        FileInputStream fileInputStream = new FileInputStream(file);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(fileInputStream));
    }

}
