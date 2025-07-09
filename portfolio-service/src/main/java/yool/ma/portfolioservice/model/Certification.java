package yool.ma.portfolioservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "certifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    @JsonBackReference
    private Profile profile;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String issuingOrganization;
    private LocalDate issueDate;
    private LocalDate expiryDate;

    private String certificateUrl;
    private String validationLink;

    private String category;

    private boolean manuallyAdded;

    @Column(nullable = true)
    private Long certifMediaId;
}