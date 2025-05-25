package yool.ma.portfolioservice.dto;

import java.util.List;

public class PortfolioSummaryDto {

    private String diploma;
    private String experienceTitle;
    private String experienceCompany;
    private List<String> skills;
    private String degree;
    private String fieldOfStudy;

    // Constructors
    public PortfolioSummaryDto() {
    }

    public PortfolioSummaryDto(String diploma, String experienceTitle, String experienceCompany, List<String> skills, String degree, String fieldOfStudy) {
        this.diploma = diploma;
        this.experienceTitle = experienceTitle;
        this.experienceCompany = experienceCompany;
        this.skills = skills;
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
    }

    // Getters and Setters
    public String getDiploma() {
        return diploma;
    }

    public void setDiploma(String diploma) {
        this.diploma = diploma;
    }

    public String getExperienceTitle() {
        return experienceTitle;
    }

    public void setExperienceTitle(String experienceTitle) {
        this.experienceTitle = experienceTitle;
    }

    public String getExperienceCompany() {
        return experienceCompany;
    }

    public void setExperienceCompany(String experienceCompany) {
        this.experienceCompany = experienceCompany;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    // toString() method (optional, for debugging)
    @Override
    public String toString() {
        return "PortfolioSummaryDto{" +
                "diploma='" + diploma + '\'' +
                ", experienceTitle='" + experienceTitle + '\'' +
                ", experienceCompany='" + experienceCompany + '\'' +
                ", skills=" + skills +
                ", degree='" + degree + '\'' +
                ", fieldOfStudy='" + fieldOfStudy + '\'' +
                '}';
    }
} 