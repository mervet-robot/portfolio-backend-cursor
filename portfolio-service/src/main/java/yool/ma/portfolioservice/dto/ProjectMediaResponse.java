package yool.ma.portfolioservice.dto;

import lombok.Data;


@Data
public class ProjectMediaResponse {
    private Long id;
    private Long projectId;
    private String fileName;
    private String filePath;
    private String fileType;
    private long fileSize;
}