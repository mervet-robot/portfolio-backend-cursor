package yool.ma.portfolioservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import yool.ma.portfolioservice.ennum.SkillLevel;

@Entity
@Table(name = "tech_skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    @JsonBackReference
    private Profile profile;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private SkillLevel level;

    private String category; // e.g., "Programming", "Database", "DevOps"
    private int yearsOfExperience;
    private boolean verified;
}
