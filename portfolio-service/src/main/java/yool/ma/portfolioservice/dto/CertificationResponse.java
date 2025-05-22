package yool.ma.portfolioservice.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CertificationResponse {
    private Long id;
    private String name;
    private String description;
    private String issuingOrganization;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String certificateUrl;
    private String validationLink;
    private String category;
    private boolean manuallyAdded;
}