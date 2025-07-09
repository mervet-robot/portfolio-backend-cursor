package yool.ma.portfolioservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yool.ma.portfolioservice.ennum.MediaType;

import java.time.LocalDateTime;

@Entity
@Table(name = "certif_media")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertifMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String fileName;
    private String filePath;
    private String fileType;
    private long fileSize;

    private String titre;
    private String description;
    private String category;
    private boolean verified;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    private LocalDateTime uploadDate;

    // Transient field for userId to be included in JSON response
    @Transient
    private Long userId;

    @PostLoad
    @PostPersist
    @PostUpdate
    private void setUserIdFromUser() {
        if (this.user != null) {
            this.userId = this.user.getId();
        }
    }

    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }
} 