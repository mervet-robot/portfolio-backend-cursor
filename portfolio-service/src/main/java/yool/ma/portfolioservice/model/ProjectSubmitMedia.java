package yool.ma.portfolioservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yool.ma.portfolioservice.ennum.MediaType;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_submit_media")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSubmitMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_submit_id")
    @JsonBackReference
    private ProjectSubmit projectSubmit;

    private String fileName;
    private String fileType;
    private String filePath;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onPersist() {
        uploadedAt = LocalDateTime.now();
    }
} 