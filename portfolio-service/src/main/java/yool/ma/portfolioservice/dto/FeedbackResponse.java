package yool.ma.portfolioservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FeedbackResponse {
    private Long id;
    private Long projectId;
    private String comment;
    private Integer technicalScore;
    private Integer attitudeScore;
    private LocalDateTime createdAt;
}