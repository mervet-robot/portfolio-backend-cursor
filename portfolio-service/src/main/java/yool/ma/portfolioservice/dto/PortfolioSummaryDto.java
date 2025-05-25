package yool.ma.portfolioservice.dto;

import java.util.List;

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

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDiploma() {
        return diploma;
    }

    public void setDiploma(String diploma) {
        this.diploma = diploma;
    }

    public List<ExperienceDetailDto> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ExperienceDetailDto> experiences) {
        this.experiences = experiences;
    }

    public FormationDetailDto getLatestFormation() {
        return latestFormation;
    }

    public void setLatestFormation(FormationDetailDto latestFormation) {
        this.latestFormation = latestFormation;
    }

    public List<String> getTechSkills() {
        return techSkills;
    }

    public void setTechSkills(List<String> techSkills) {
        this.techSkills = techSkills;
    }

    public List<CertificationDetailDto> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<CertificationDetailDto> certifications) {
        this.certifications = certifications;
    }

    public List<ProjectDetailDto> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDetailDto> projects) {
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