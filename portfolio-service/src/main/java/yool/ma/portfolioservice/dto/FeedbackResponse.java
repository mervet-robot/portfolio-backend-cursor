package yool.ma.portfolioservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FeedbackResponse {
    private Long id;
    private Long projectId;
    private Long reviewerId;
    private String comment;
    private Integer technicalScore;
    private Integer attitudeScore;
    private LocalDateTime createdAt;
}