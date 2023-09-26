package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Reply {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int _iId;

    @NotNull
    @Column(name = "creationDate")
    private LocalDateTime _tCreationDate;

    @NotNull
    @Column(name = "text")
    private String _sText;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person _person;

    @OneToOne
    @JoinColumn(name = "image_id")
    private ImagePath _imagePath;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket _ticket;

    public Reply() {}

    public Reply(String sText, Person person, Ticket ticket) {
        _tCreationDate = LocalDateTime.now();
        _sText = sText;
        _person = person;
        _ticket = ticket;
    }

    public int get_iId() {
        return _iId;
    }

    public LocalDateTime get_tCreationDate() {
        return _tCreationDate;
    }

    public String get_sText() {
        return _sText;
    }

    public Person get_person() {
        return _person;
    }

    public ImagePath get_imagePath() {
        return _imagePath;
    }

    public void set_imagePath(ImagePath imagePath) {
        _imagePath = imagePath;
    }
    public Ticket get_ticket() {
        return _ticket;
    }
}
