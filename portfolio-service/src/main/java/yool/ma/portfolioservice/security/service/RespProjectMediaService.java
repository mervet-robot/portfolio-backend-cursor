package yool.ma.portfolioservice.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import yool.ma.portfolioservice.dto.RespProjectMediaResponse;
import yool.ma.portfolioservice.ennum.MediaType;

import yool.ma.portfolioservice.model.RespProject;
import yool.ma.portfolioservice.model.RespProjectMedia;

import yool.ma.portfolioservice.repository.RespProjectMediaRepository;
import yool.ma.portfolioservice.repository.RespProjectRepository;
import yool.ma.portfolioservice.security.exception.FileStorageException;
import yool.ma.portfolioservice.security.exception.ResourceNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class RespProjectMediaService {

    @Autowired
    private RespProjectMediaRepository resProjectMediaRepository;

    @Autowired
    private RespProjectRepository projectRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Add media to a project
     */
    public RespProjectMediaResponse addRespProjectMedia(Long projectId, MultipartFile file, MediaType mediaType) {
        try {
            RespProject project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

            // Create directory if it doesn't exist
            String projectDirectory = uploadDir + File.separator + projectId;
            File directory = new File(projectDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate unique filename to avoid conflicts
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = System.currentTimeMillis() + "_" + originalFilename;
            String filePath = projectDirectory + File.separator + fileName;

            // Save file to disk
            Path targetLocation = Paths.get(filePath);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Create and save media entity
            RespProjectMedia media = new RespProjectMedia();
            media.setRespProject(project);
            media.setFileName(originalFilename);
            media.setFilePath(filePath);
            media.setFileType(file.getContentType());
            media.setFileSize(file.getSize());
            media.setMediaType(mediaType);

            RespProjectMedia savedMedia = resProjectMediaRepository.save(media);
            return mapToResponse(savedMedia);
        } catch (IOException ex) {
            log.error("Could not store file: {}", ex.getMessage());
            throw new FileStorageException("Could not store file. Please try again!", ex);
        }
    }

    /**
     * Get all media for a project
     */
    public List<RespProjectMediaResponse> getRespProjectMediaByProject(Long projectId) {
        RespProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        return resProjectMediaRepository.findByRespProject(project)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get media by project and media type
     */
    public List<RespProjectMediaResponse> getRespProjectMediaByType(Long projectId, MediaType mediaType) {
        RespProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        return resProjectMediaRepository.findByRespProjectAndMediaType(project, mediaType)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get file content by media id
     */
    public byte[] getRespProjectMediaContent(Long mediaId) {
        RespProjectMedia media = resProjectMediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + mediaId));

        try {
            Path path = Paths.get(media.getFilePath());
            return Files.readAllBytes(path);
        } catch (IOException ex) {
            log.error("Could not read file: {}", ex.getMessage());
            throw new FileStorageException("Could not read file. Please try again!", ex);
        }
    }

    public RespProjectMedia getRespProjectMediaById(Long mediaId) {
        return resProjectMediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + mediaId));
    }

    /**
     * Delete project media
     */
    public void deleteRespProjectMedia(Long mediaId) {
        RespProjectMedia media = resProjectMediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + mediaId));

        try {
            // Delete file from disk
            Path path = Paths.get(media.getFilePath());
            Files.deleteIfExists(path);

            // Delete from database
            resProjectMediaRepository.delete(media);
        } catch (IOException ex) {
            log.error("Could not delete file: {}", ex.getMessage());
            throw new FileStorageException("Could not delete file. Please try again!", ex);
        }
    }

    private RespProjectMediaResponse mapToResponse(RespProjectMedia media) {
        RespProjectMediaResponse response = new RespProjectMediaResponse();
        response.setId(media.getId());
        if (media.getRespProject() != null) {
            response.setRespProjectId(media.getRespProject().getId());
        }
        response.setFileName(media.getFileName());
        response.setFilePath(media.getFilePath());
        response.setFileType(media.getFileType());
        response.setFileSize(media.getFileSize());
        response.setMediaType(media.getMediaType());
        response.setUploadDate(media.getUploadDate());
        return response;
    }
}
