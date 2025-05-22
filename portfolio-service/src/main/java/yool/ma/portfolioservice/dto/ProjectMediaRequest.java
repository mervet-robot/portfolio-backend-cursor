package yool.ma.portfolioservice.dto;

import lombok.Data;

@Data
public class ProjectMediaRequest {
    private String fileName;
    private String filePath;
    private String fileType;
    private long fileSize;
}
