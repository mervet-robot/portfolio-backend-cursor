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
public class CertifMediaService {

    @Autowired
    private CertifMediaRepository certifMediaRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Add media to a user
     */
    public CertifMedia addCertifMedia(String username, MultipartFile file, MediaType mediaType,
                                     String titre, String description, String category, boolean verified) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

            // Create directory if it doesn't exist
            String userDirectory = uploadDir + File.separator + username;
            File directory = new File(userDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate unique filename to avoid conflicts
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = System.currentTimeMillis() + "_" + originalFilename;
            String filePath = userDirectory + File.separator + fileName;

            // Save file to disk
            Path targetLocation = Paths.get(filePath);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Create and save media entity
            CertifMedia media = new CertifMedia();
            media.setUser(user);
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

            return certifMediaRepository.save(media);
        } catch (IOException ex) {
            log.error("Could not store file: {}", ex.getMessage());
            throw new FileStorageException("Could not store file. Please try again!", ex);
        }
    }

    /**
     * Get all media for a user
     */
    public List<CertifMedia> getCertifMediaByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        return certifMediaRepository.findByUser(user);
    }

    /**
     * Get a single media item by its ID.
     */
    public CertifMedia getCertifMediaById(Long mediaId) {
        return certifMediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + mediaId));
    }

    /**
     * Get media by user and media type
     */
    public List<CertifMedia> getCertifMediaByType(String username, MediaType mediaType) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        return certifMediaRepository.findByUserAndMediaType(user, mediaType);
    }

    /**
     * Get file content by media id
     */
    public byte[] getCertifMediaContent(Long mediaId) {
        CertifMedia media = certifMediaRepository.findById(mediaId)
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
     * Delete certif media
     */
    public void deleteCertifMedia(Long mediaId) {
        CertifMedia media = certifMediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + mediaId));

        try {
            // Delete file from disk
            Path path = Paths.get(media.getFilePath());
            Files.deleteIfExists(path);

            // Delete from database
            certifMediaRepository.delete(media);
        } catch (IOException ex) {
            log.error("Could not delete file: {}", ex.getMessage());
            throw new FileStorageException("Could not delete file. Please try again!", ex);
        }
    }

    /**
     *ADD FOR FILTER AND SEARCH
     * */

    public List<CertifMedia> getCertifMediaByCategory(String username, String category) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return certifMediaRepository.findByUserAndCategory(user, category);
    }

    public List<CertifMedia> getCertifMediaByTypeAndCategory(String username, MediaType mediaType, String category) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return certifMediaRepository.findByUserAndMediaTypeAndCategory(user, mediaType, category);
    }

    public List<CertifMedia> searchCertifMediaByTitle(String username, String title) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return certifMediaRepository.findByUserAndTitreContainingIgnoreCase(user, title);
    }
} 