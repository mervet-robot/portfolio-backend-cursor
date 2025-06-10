package yool.ma.portfolioservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yool.ma.portfolioservice.ennum.MediaType;

import java.time.LocalDateTime;


@Entity
@Table(name = "resp_project_media")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespProjectMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resp_project_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private RespProject respProject;

    private String fileName;
    private String filePath;
    private String fileType;  // Store mime type (e.g., image/jpeg, application/pdf)
    private long fileSize;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;  // Higher-level type category

    private LocalDateTime uploadDate;

    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }
} 