package yool.ma.portfolioservice.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import yool.ma.portfolioservice.ennum.MediaType;
import yool.ma.portfolioservice.model.Project;
import yool.ma.portfolioservice.model.ProjectMedia;
import yool.ma.portfolioservice.repository.ProjectMediaRepository;
import yool.ma.portfolioservice.repository.ProjectRepository;
import yool.ma.portfolioservice.security.exception.FileStorageException;
import yool.ma.portfolioservice.security.exception.ResourceNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@Slf4j
public class ProjectMediaService {

    @Autowired
    private ProjectMediaRepository projectMediaRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Add media to a project
     */
    public ProjectMedia addProjectMedia(Long projectId, MultipartFile file, MediaType mediaType) {
        try {
            Project project = projectRepository.findById(projectId)
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
            ProjectMedia media = new ProjectMedia();
            media.setProject(project);
            media.setFileName(originalFilename);
            media.setFilePath(filePath);
            media.setFileType(file.getContentType());
            media.setFileSize(file.getSize());
            media.setMediaType(mediaType);

            return projectMediaRepository.save(media);
        } catch (IOException ex) {
            log.error("Could not store file: {}", ex.getMessage());
            throw new FileStorageException("Could not store file. Please try again!", ex);
        }
    }

    /**
     * Get all media for a project
     */
    public List<ProjectMedia> getProjectMediaByProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        return projectMediaRepository.findByProject(project);
    }

    /**
     * Get media by project and media type
     */
    public List<ProjectMedia> getProjectMediaByType(Long projectId, MediaType mediaType) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        return projectMediaRepository.findByProjectAndMediaType(project, mediaType);
    }

    /**
     * Get file content by media id
     */
    public byte[] getProjectMediaContent(Long mediaId) {
        ProjectMedia media = projectMediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + mediaId));

        try {
            Path path = Paths.get(media.getFilePath());
            return Files.readAllBytes(path);
        } catch (IOException ex) {
            log.error("Could not read file: {}", ex.getMessage());
            throw new FileStorageException("Could not read file. Please try again!", ex);
        }
    }

    /**
     * Delete project media
     */
    public void deleteProjectMedia(Long mediaId) {
        ProjectMedia media = projectMediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + mediaId));

        try {
            // Delete file from disk
            Path path = Paths.get(media.getFilePath());
            Files.deleteIfExists(path);

            // Delete from database
            projectMediaRepository.delete(media);
        } catch (IOException ex) {
            log.error("Could not delete file: {}", ex.getMessage());
            throw new FileStorageException("Could not delete file. Please try again!", ex);
        }
    }
}
