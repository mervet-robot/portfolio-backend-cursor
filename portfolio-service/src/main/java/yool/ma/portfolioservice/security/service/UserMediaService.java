package yool.ma.portfolioservice.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import yool.ma.portfolioservice.ennum.MediaType;
import yool.ma.portfolioservice.model.*;
import yool.ma.portfolioservice.repository.*;
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
public class UserMediaService {

    @Autowired
    private UserMediaRepository userMediaRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Add media to a project
     */
    public UserMedia addProjectMedia(Long profileId, MultipartFile file, MediaType mediaType,
                                     String titre, String description, String category, boolean verified) {
        try {
            Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + profileId));

            // Create directory if it doesn't exist
            String profileDirectory = uploadDir + File.separator + profileId;
            File directory = new File(profileDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate unique filename to avoid conflicts
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = System.currentTimeMillis() + "_" + originalFilename;
            String filePath = profileDirectory + File.separator + fileName;

            // Save file to disk
            Path targetLocation = Paths.get(filePath);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Create and save media entity
            UserMedia media = new UserMedia();
            media.setProfile(profile);
            media.setFileName(originalFilename);
            media.setFilePath(filePath);
            media.setFileType(file.getContentType());
            media.setFileSize(file.getSize());

            // Set the new attributes
            media.setTitre(titre);
            media.setDescription(description);
            media.setCategory(category);
            media.setVerified(verified);


            media.setMediaType(mediaType);

            return userMediaRepository.save(media);
        } catch (IOException ex) {
            log.error("Could not store file: {}", ex.getMessage());
            throw new FileStorageException("Could not store file. Please try again!", ex);
        }
    }

    /**
     * Get all media for a project
     */
    public List<UserMedia> getProjectMediaByProject(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + profileId));

        return userMediaRepository.findByProfile(profile);
    }

    /**
     * Get media by project and media type
     */
    public List<UserMedia> getProjectMediaByType(Long profileId, MediaType mediaType) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + profileId));

        return userMediaRepository.findByProfileAndMediaType(profile, mediaType);
    }

    /**
     * Get file content by media id
     */
    public byte[] getProjectMediaContent(Long mediaId) {
        UserMedia media = userMediaRepository.findById(mediaId)
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
        UserMedia media = userMediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + mediaId));

        try {
            // Delete file from disk
            Path path = Paths.get(media.getFilePath());
            Files.deleteIfExists(path);

            // Delete from database
            userMediaRepository.delete(media);
        } catch (IOException ex) {
            log.error("Could not delete file: {}", ex.getMessage());
            throw new FileStorageException("Could not delete file. Please try again!", ex);
        }
    }

    /**
     *ADD FOR FILTER AND SEARCH
     * */

    public List<UserMedia> getProjectMediaByCategory(Long profileId, String category) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + profileId));
        return userMediaRepository.findByProfileAndCategory(profile, category);
    }

    public List<UserMedia> getProjectMediaByTypeAndCategory(Long profileId, MediaType mediaType, String category) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + profileId));
        return userMediaRepository.findByProfileAndMediaTypeAndCategory(profile, mediaType, category);
    }

    public List<UserMedia> searchProjectMediaByTitle(Long profileId, String title) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + profileId));
        return userMediaRepository.findByProfileAndTitreContainingIgnoreCase(profile, title);
    }


}