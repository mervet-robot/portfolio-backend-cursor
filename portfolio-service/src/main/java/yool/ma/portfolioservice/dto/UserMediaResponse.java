package yool.ma.portfolioservice.dto;

import lombok.Data;

@Data
public class UserMediaResponse {
    private Long id;
    private Long profileId;
    private String fileName;
    private String filePath;
    private String fileType;
    private long fileSize;

    private String titre;
    private String description;  // Added attribute for document description
    private String category;     // Added attribute for organizing documents by category
    private boolean verified;
}
