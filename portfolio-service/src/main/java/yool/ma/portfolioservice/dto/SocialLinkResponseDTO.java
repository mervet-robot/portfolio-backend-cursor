package yool.ma.portfolioservice.dto;

import lombok.Data;

@Data
public class SocialLinkResponseDTO {
    private Long id;
    private String platform;
    private String url;
    // private Long profileId; // Optionally include profileId if needed by frontend
} 