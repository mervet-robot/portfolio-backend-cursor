package yool.ma.portfolioservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import yool.ma.portfolioservice.ennum.ProjectSubmitStatus;

@Data
public class ProjectReviewRequest {
    @NotNull
    private ProjectSubmitStatus status;
} 