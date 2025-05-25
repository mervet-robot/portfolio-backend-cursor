package yool.ma.portfolioservice.dto;

public class CertificationDetailDto {
    private String name;
    private String issuingOrganization;

    // Constructors
    public CertificationDetailDto() {
    }

    public CertificationDetailDto(String name, String issuingOrganization) {
        this.name = name;
        this.issuingOrganization = issuingOrganization;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssuingOrganization() {
        return issuingOrganization;
    }

    public void setIssuingOrganization(String issuingOrganization) {
        this.issuingOrganization = issuingOrganization;
    }
} 