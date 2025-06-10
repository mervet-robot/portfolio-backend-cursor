package yool.ma.portfolioservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FormationDetailDto {
    private String degree;
    private String institution;
    private String fieldOfStudy;

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean current;
    }