package yool.ma.portfolioservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "social_links")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String platform; // e.g., "LinkedIn", "GitHub", "Twitter"

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @JsonIgnore // To avoid circular dependencies during serialization
    private Profile profile;

    // Constructors, getters, setters, etc. will be handled by Lombok
} 