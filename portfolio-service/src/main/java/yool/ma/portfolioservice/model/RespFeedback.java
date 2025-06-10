package yool.ma.portfolioservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "resp_feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resp_project_id")
    private RespProject respProject;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;

    private Integer technicalScore;
    private Integer attitudeScore;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 