package yool.ma.portfolioservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yool.ma.portfolioservice.ennum.ProjectSubmitStatus;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "project_submits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSubmit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "apprenant_id", nullable = false)
    @JsonBackReference
    private User apprenant;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ProjectSubmitStatus status;

    @ElementCollection
    @CollectionTable(name = "project_submit_skills", joinColumns = @JoinColumn(name = "project_submit_id"))
    @Column(name = "skill")
    private Set<String> skills = new HashSet<>();

    @OneToMany(mappedBy = "projectSubmit", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectSubmitMedia> mediaFiles = new HashSet<>();

    @OneToMany(mappedBy = "projectSubmit", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectSubmitFeedback> feedbacks = new HashSet<>();
} 