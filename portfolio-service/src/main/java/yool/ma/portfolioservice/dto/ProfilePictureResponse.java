package yool.ma.portfolioservice.dto;

import lombok.Data;

@Data
public class ProfilePictureResponse {
    private String message;
    private String filePath;
    private Long profileId;
}