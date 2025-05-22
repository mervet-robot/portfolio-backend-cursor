package yool.ma.portfolioservice.dto;

import lombok.Data;
import yool.ma.portfolioservice.ennum.EmploymentType;
import java.time.LocalDate;

@Data
public class ExperienceResponse {
    private Long id;
    private String title;
    private String company;
    private String location;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean current;
    private EmploymentType employmentType;
}
