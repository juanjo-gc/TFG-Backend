package es.uca.tfg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue
    int _iId;

    @NotNull
    @Column(name = "text")
    private String _sText;

    @NotNull
    @Column(name = "sentAt")
    private LocalDateTime _tSentAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "issuer_id")
    private User _issuer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User _recipient;

    @Column(name = "seen")
    private boolean _bSeen;

    public Message() {}

    public Message(String sText, User issuer, User recipient) {
        _sText = sText;
        _issuer = issuer;
        _recipient = recipient;
        _tSentAt = LocalDateTime.now();
        _bSeen = false;
    }

    public int get_iId() {
        return _iId;
    }


    public String get_sText() {
        return _sText;
    }

    public void set_sText(String sText) {
        _sText = sText;
    }

    public LocalDateTime get_tSentAt() {
        return _tSentAt;
    }

    public User get_issuer() {
        return _issuer;
    }

    public void set_issuer(User issuer) {
        _issuer = issuer;
    }

    public User get_recipient() {
        return _recipient;
    }

    public void set_recipient(User recipient) {
        _recipient = recipient;
    }

    public boolean is_bSeen() {
        return _bSeen;
    }

    public void set_bSeen(boolean bSeen) {
        _bSeen = bSeen;
    }
}
