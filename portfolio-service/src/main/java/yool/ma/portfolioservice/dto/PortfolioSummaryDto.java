package yool.ma.portfolioservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class PortfolioSummaryDto {

    // Profile fields
    private String firstName;
    private String lastName;
    private String diploma; // This is from profile, like a headline or current profession

    // Collections of details
    private List<ExperienceDetailDto> experiences;
    private FormationDetailDto latestFormation; // Only the latest formation
    private List<String> techSkills; // List of skill names
    private List<CertificationDetailDto> certifications;
    private List<ProjectDetailDto> projects;

    // Constructors
    public PortfolioSummaryDto() {
    }

    public PortfolioSummaryDto(String firstName, String lastName, String diploma,
                               List<ExperienceDetailDto> experiences, FormationDetailDto latestFormation,
                               List<String> techSkills, List<CertificationDetailDto> certifications,
                               List<ProjectDetailDto> projects) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.diploma = diploma;
        this.experiences = experiences;
        this.latestFormation = latestFormation;
        this.techSkills = techSkills;
        this.certifications = certifications;
        this.projects = projects;
    }


    // toString() method (optional, for debugging)
    @Override
    public String toString() {
        return "PortfolioSummaryDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", diploma='" + diploma + '\'' +
                ", experiences=" + experiences +
                ", latestFormation=" + latestFormation +
                ", techSkills=" + techSkills +
                ", certifications=" + certifications +
                ", projects=" + projects +
                '}';
    }
} 