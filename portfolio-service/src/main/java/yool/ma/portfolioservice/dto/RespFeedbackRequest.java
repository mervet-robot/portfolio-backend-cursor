package yool.ma.portfolioservice.dto;

import lombok.Data;

@Data
public class RespFeedbackRequest {
    private Long respProjectId;
    private Long reviewerId;
    private String comment;
    private Integer technicalScore;
    private Integer attitudeScore;
} 