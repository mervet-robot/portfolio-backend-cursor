package yool.ma.portfolioservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RespFeedbackResponse {
    private Long id;
    private Long respProjectId;
    private Long reviewerId;
    private String comment;
    private Integer technicalScore;
    private Integer attitudeScore;
    private LocalDateTime createdAt;
} 