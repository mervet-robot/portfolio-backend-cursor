package yool.ma.portfolioservice.dto;

import lombok.Data;
import yool.ma.portfolioservice.ennum.MediaType;
import java.time.LocalDateTime;

@Data
public class RespProjectMediaResponse {
    private Long id;
    private Long respProjectId;
    private String fileName;
    private String filePath;
    private String fileType;
    private long fileSize;
    private MediaType mediaType;
    private LocalDateTime uploadDate;
} 