package yool.ma.portfolioservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yool.ma.portfolioservice.ennum.MediaType;

import java.time.LocalDateTime;


@Entity
    @Table(name = "user_media")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class UserMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
//    @JsonBackReference
    private Profile profile;

    private String fileName;
    private String filePath;
    private String fileType;  // Store mime type (e.g., image/jpeg, application/pdf)
    private long fileSize;

    private String titre;
    private String description;  // Added attribute for document description
    private String category;     // Added attribute for organizing documents by category
    private boolean verified;


    @Enumerated(EnumType.STRING)
    private MediaType mediaType;  // Higher-level type category

    private LocalDateTime uploadDate;

    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }
}