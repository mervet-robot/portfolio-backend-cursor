package yool.ma.portfolioservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FeedbackRequest {
    private Long projectId;
    private Long reviewerId;
    private String comment;
    private Integer technicalScore;
    private Integer attitudeScore;
}
