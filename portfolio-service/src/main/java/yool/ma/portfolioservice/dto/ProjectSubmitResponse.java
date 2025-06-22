package yool.ma.portfolioservice.dto;

import lombok.Data;
import yool.ma.portfolioservice.ennum.ProjectSubmitStatus;

import java.time.LocalDate;
import java.util.Set;

@Data
public class ProjectSubmitResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectSubmitStatus status;
    private Set<String> skills;
    private Long profileId;
    private Long projectId;
} 