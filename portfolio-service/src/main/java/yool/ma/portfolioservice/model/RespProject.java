package yool.ma.portfolioservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yool.ma.portfolioservice.ennum.ProjectStatus;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "resp_projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    @JsonBackReference
    private User responsable;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @ElementCollection
    @CollectionTable(name = "resp_project_skills", joinColumns = @JoinColumn(name = "resp_project_id"))
    @Column(name = "skill")
    private Set<String> skills = new HashSet<>();

    @OneToMany(mappedBy = "respProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RespProjectMedia> mediaFiles = new HashSet<>();

    @OneToMany(mappedBy = "respProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RespFeedback> feedbacks = new HashSet<>();
} 