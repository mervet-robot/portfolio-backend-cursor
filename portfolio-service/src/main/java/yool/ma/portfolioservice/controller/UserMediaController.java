package yool.ma.portfolioservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import yool.ma.portfolioservice.ennum.MediaType;
import yool.ma.portfolioservice.model.UserMedia;
import yool.ma.portfolioservice.security.service.UserMediaService;


import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user-media")
public class UserMediaController {

    @Autowired
    private UserMediaService userMediaService;

    @PostMapping("/{profileId}/upload")
//    @PreAuthorize("hasRole('APPRENANT') or hasRole('RESPONSABLE')")
    public ResponseEntity<UserMedia> uploadFile(
            @PathVariable Long profileId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("mediaType") MediaType mediaType,
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("verified") boolean verified) {

        UserMedia media = userMediaService.addProjectMedia(profileId, file, mediaType, titre, description, category, verified);
        return ResponseEntity.ok(media);
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<List<UserMedia>> getUserMedia(@PathVariable Long profileId) {
        List<UserMedia> mediaList = userMediaService.getProjectMediaByProject(profileId);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/{profileId}/type/{mediaType}")
    public ResponseEntity<List<UserMedia>> getProjectMediaByType(
            @PathVariable Long profileId,
            @PathVariable MediaType mediaType) {

        List<UserMedia> mediaList = userMediaService.getProjectMediaByType(profileId, mediaType);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/download/{mediaId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long mediaId) {
        UserMedia media = userMediaService.getProjectMediaByProject(mediaId).stream()
                .filter(m -> m.getId().equals(mediaId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Media not found"));

        byte[] fileContent = userMediaService.getProjectMediaContent(mediaId);

        ByteArrayResource resource = new ByteArrayResource(fileContent);

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(media.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + media.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<?> deleteMedia(@PathVariable Long mediaId) {
        userMediaService.deleteProjectMedia(mediaId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/media-types")
    public ResponseEntity<MediaType[]> getMediaTypes() {
        return ResponseEntity.ok(MediaType.values());
    }


    /**
     *ADD FOR FILTER AND SEARCH
     * */

    @GetMapping("/{profileId}/category/{category}")
    public ResponseEntity<List<UserMedia>> getProjectMediaByCategory(
            @PathVariable Long profileId,
            @PathVariable String category) {
        List<UserMedia> mediaList = userMediaService.getProjectMediaByCategory(profileId, category);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/{profileId}/type/{mediaType}/category/{category}")
    public ResponseEntity<List<UserMedia>> getProjectMediaByTypeAndCategory(
            @PathVariable Long profileId,
            @PathVariable MediaType mediaType,
            @PathVariable String category) {
        List<UserMedia> mediaList = userMediaService.getProjectMediaByTypeAndCategory(profileId, mediaType, category);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/{profileId}/search")
    public ResponseEntity<List<UserMedia>> searchProjectMediaByTitle(
            @PathVariable Long profileId,
            @RequestParam String title) {
        List<UserMedia> mediaList = userMediaService.searchProjectMediaByTitle(profileId, title);
        return ResponseEntity.ok(mediaList);
    }

}

