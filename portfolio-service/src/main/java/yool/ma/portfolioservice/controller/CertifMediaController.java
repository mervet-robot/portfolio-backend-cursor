package yool.ma.portfolioservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import yool.ma.portfolioservice.ennum.MediaType;
import yool.ma.portfolioservice.model.CertifMedia;
import yool.ma.portfolioservice.security.service.CertifMediaService;


import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/certif-media")
public class CertifMediaController {

    @Autowired
    private CertifMediaService certifMediaService;

    @PostMapping("/{username}/upload")
    public ResponseEntity<CertifMedia> uploadFile(
            @PathVariable String username,
            @RequestParam("file") MultipartFile file,
            @RequestParam("mediaType") MediaType mediaType,
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("verified") boolean verified) {

        CertifMedia media = certifMediaService.addCertifMedia(username, file, mediaType, titre, description, category, verified);
        return ResponseEntity.ok(media);
    }



    @GetMapping("/{username}")
    public ResponseEntity<List<CertifMedia>> getCertifMedia(@PathVariable String username) {
        List<CertifMedia> mediaList = certifMediaService.getCertifMediaByUser(username);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/{username}/type/{mediaType}")
    public ResponseEntity<List<CertifMedia>> getCertifMediaByType(
            @PathVariable String username,
            @PathVariable MediaType mediaType) {

        List<CertifMedia> mediaList = certifMediaService.getCertifMediaByType(username, mediaType);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/download/{mediaId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long mediaId) {
        CertifMedia media = certifMediaService.getCertifMediaById(mediaId);

        byte[] fileContent = certifMediaService.getCertifMediaContent(mediaId);

        ByteArrayResource resource = new ByteArrayResource(fileContent);

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(media.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + media.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<?> deleteMedia(@PathVariable Long mediaId) {
        certifMediaService.deleteCertifMedia(mediaId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/media-types")
    public ResponseEntity<MediaType[]> getMediaTypes() {
        return ResponseEntity.ok(MediaType.values());
    }


    /**
     *ADD FOR FILTER AND SEARCH
     * */

    @GetMapping("/{username}/category/{category}")
    public ResponseEntity<List<CertifMedia>> getCertifMediaByCategory(
            @PathVariable String username,
            @PathVariable String category) {
        List<CertifMedia> mediaList = certifMediaService.getCertifMediaByCategory(username, category);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/{username}/type/{mediaType}/category/{category}")
    public ResponseEntity<List<CertifMedia>> getCertifMediaByTypeAndCategory(
            @PathVariable String username,
            @PathVariable MediaType mediaType,
            @PathVariable String category) {
        List<CertifMedia> mediaList = certifMediaService.getCertifMediaByTypeAndCategory(username, mediaType, category);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/{username}/search")
    public ResponseEntity<List<CertifMedia>> searchCertifMediaByTitle(
            @PathVariable String username,
            @RequestParam String title) {
        List<CertifMedia> mediaList = certifMediaService.searchCertifMediaByTitle(username, title);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CertifMedia>> getAllCertifMedia() {
        List<CertifMedia> mediaList = certifMediaService.getAllCertifMedia();
        return ResponseEntity.ok(mediaList);
    }
} 