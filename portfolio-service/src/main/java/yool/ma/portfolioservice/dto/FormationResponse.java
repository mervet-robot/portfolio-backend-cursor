package yool.ma.portfolioservice.dto;


import lombok.Data;
import java.time.LocalDate;

@Data
public class FormationResponse {
    private Long id;
    private String degree;
    private String institution;
    private String fieldOfStudy;
    private String location;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean current;
    private String grade;
    private String activities;
    private String diplomaUrl;
}
