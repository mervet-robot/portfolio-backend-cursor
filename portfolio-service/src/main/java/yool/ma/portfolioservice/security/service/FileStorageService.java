package yool.ma.portfolioservice.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import yool.ma.portfolioservice.ennum.MediaType;
import yool.ma.portfolioservice.security.exception.FileStorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

//@Service
//public class FileStorageService {
//
//    @Value("${file.upload-dir}")
//    private String uploadDir;
//
//    public String storeFile(MultipartFile file, MediaType mediaType) {
//        // Check if the file's mediaType matches the requested mediaType
//        String mimeType = file.getContentType();
//        MediaType detectedType = MediaType.fromMimeType(mimeType);
//
//        if (detectedType == null || detectedType != mediaType) {
//            throw new FileStorageException("Invalid file type. Expected " + mediaType + " but got " + mimeType);
//        }
//
//        try {
//            // Create upload directory if it doesn't exist
//            Path uploadPath = Paths.get(uploadDir + "/" + mediaType.name().toLowerCase());
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            // Create unique filename
//            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
//            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            String filename = UUID.randomUUID().toString() + fileExtension;
//
//            // Save the file
//            Path targetLocation = uploadPath.resolve(filename);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//            return mediaType.name().toLowerCase() + "/" + filename;
//        } catch (IOException ex) {
//            throw new FileStorageException("Could not store file. Please try again!", ex);
//        }
//    }
//
//    public byte[] loadFileAsBytes(String filePath) {
//        try {
//            Path fullPath = Paths.get(uploadDir + "/" + filePath);
//            return Files.readAllBytes(fullPath);
//        } catch (IOException ex) {
//            throw new FileStorageException("Could not load file", ex);
//        }
//    }
//
//    public void deleteFile(String filePath) {
//        try {
//            Path fullPath = Paths.get(uploadDir + "/" + filePath);
//            Files.deleteIfExists(fullPath);
//        } catch (IOException ex) {
//            throw new FileStorageException("Could not delete file", ex);
//        }
//    }
//}
