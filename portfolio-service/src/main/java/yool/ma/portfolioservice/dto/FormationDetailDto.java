package yool.ma.portfolioservice.dto;

public class FormationDetailDto {
    private String degree;
    private String institution;
    private String fieldOfStudy;

    // Constructors
    public FormationDetailDto() {
    }

    public FormationDetailDto(String degree, String institution, String fieldOfStudy) {
        this.degree = degree;
        this.institution = institution;
        this.fieldOfStudy = fieldOfStudy;
    }

    // Getters and Setters
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }
} 