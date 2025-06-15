package yool.ma.portfolioservice.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class ProjectSubmitRequest {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<String> skills;
    // Status will be set by the backend, not the client
} 