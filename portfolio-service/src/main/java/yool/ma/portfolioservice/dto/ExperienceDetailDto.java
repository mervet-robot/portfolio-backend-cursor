package yool.ma.portfolioservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExperienceDetailDto {
    private String title;
    private String company;

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean current;

} 