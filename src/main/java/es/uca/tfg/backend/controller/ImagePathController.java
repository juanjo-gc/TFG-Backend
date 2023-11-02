package es.uca.tfg.backend.controller;

import es.uca.tfg.backend.entity.*;
import es.uca.tfg.backend.repository.*;
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
import java.time.LocalDateTime;
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
    @Autowired
    private TicketRepository _ticketRepository;
    @Autowired
    private ReplyRepository _replyRepository;

    private String _sEventsUploadPath = new FileSystemResource("").getFile().getAbsolutePath() + "\\src\\main\\resources\\static\\images\\events\\";
    private String _sTicketsUploadPath = new FileSystemResource("").getFile().getAbsolutePath() + "\\src\\main\\resources\\static\\images\\tickets\\";
    private String _sReplyUploadPath = new FileSystemResource("").getFile().getAbsolutePath() + "\\src\\main\\resources\\static\\images\\replies\\";





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

    @PostMapping("/uploadTicketImage")
    public boolean saveTicketImage(@RequestParam("id") int iTicketId, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        Optional<Ticket> optionalTicket = _ticketRepository.findById(iTicketId);
        Path path = Paths.get(_sTicketsUploadPath);

        if(optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            /*
            if (ticket.get_imagePath() != null) {
                File file = new File(_sTicketsUploadPath + event.get_headerPhoto().get_sName());
                file.delete();
            }
             */

            String sFilename = ticket.get_iId() + "-" + multipartFile.getOriginalFilename();
            File file = new File(_sTicketsUploadPath + sFilename);
            System.out.println("Guardando la imagen con nombre: " + sFilename);
            System.out.println("Ruta: " + path.toString());
            multipartFile.transferTo(file);

            ImagePath imagePath = new ImagePath(sFilename);
            imagePath = _imagePathRepository.save(imagePath);
            ticket.set_imagePath(imagePath);
            ticket = _ticketRepository.save(ticket);
            return ticket.get_imagePath() != null ? true : false;
        } else {
            return false;
        }
    }

    @PostMapping("/uploadReplyImage")
    public boolean saveReplyImage(@RequestParam("id") int iReplyId, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        Optional<Reply> optionalReply = _replyRepository.findById(iReplyId);
        Path path = Paths.get(_sReplyUploadPath);

        if(optionalReply.isPresent()) {
            Reply reply = optionalReply .get();
            /*
            if (ticket.get_imagePath() != null) {
                File file = new File(_sTicketsUploadPath + event.get_headerPhoto().get_sName());
                file.delete();
            }
             */

            String sFilename = reply.get_iId() + "-" + multipartFile.getOriginalFilename();
            File file = new File(_sReplyUploadPath + sFilename);
            System.out.println("Guardando la imagen con nombre: " + sFilename);
            System.out.println("Ruta: " + path.toString());
            multipartFile.transferTo(file);

            ImagePath imagePath = new ImagePath(sFilename);
            imagePath = _imagePathRepository.save(imagePath);
            reply.set_imagePath(imagePath);
            reply = _replyRepository.save(reply);
            return reply.get_imagePath() != null ? true : false;
        } else {
            return false;
        }
    }

    @GetMapping("/getTicketImage/{id}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getTicketImage(@PathVariable("id") int iTicketId) throws FileNotFoundException {
        Optional<Ticket> optionalTicket = _ticketRepository.findById(iTicketId);

        File file = new File(_sTicketsUploadPath + optionalTicket.get().get_imagePath().get_sName());
        FileInputStream fileInputStream = new FileInputStream(file);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(fileInputStream));
    }

    @GetMapping("/getReplyImage/{id}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getReplyImage(@PathVariable("id") int iReplyId) throws FileNotFoundException {
        Optional<Reply> optionalReply = _replyRepository.findById(iReplyId);
        File file = new File(_sReplyUploadPath + optionalReply.get().get_imagePath().get_sName());
        FileInputStream fileInputStream = new FileInputStream(file);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(fileInputStream));
    }

    @GetMapping("/getUploader/{imageId}")
    public User getUploader(@PathVariable("imageId") int iImageId) {
        Optional<ImagePath> optionalImage = _imagePathRepository.findById(iImageId);
        return optionalImage.isPresent() ? optionalImage.get().get_user() : new User();
    }

    @PatchMapping("/softDeleteOrRestoreImage/{imageId}")
    public boolean softDeleteOrRestoreImage(@PathVariable("imageId") int iImageId) {
        Optional<ImagePath> optionalImage = _imagePathRepository.findById(iImageId);
        if (optionalImage.isPresent()) {
            ImagePath image = optionalImage.get();
            if (image.get_tDeleteDate() == null) {
                image.set_tDeleteDate(LocalDateTime.now());
            } else {
                image.set_tDeleteDate(null);
            }
            _imagePathRepository.save(image);
            return image.get_tDeleteDate() != null;
        } else {
            return false;
        }
    }
}
