package yool.ma.portfolioservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yool.ma.portfolioservice.dto.RespProjectMediaResponse;
import yool.ma.portfolioservice.ennum.MediaType;
import yool.ma.portfolioservice.model.RespProjectMedia;
import yool.ma.portfolioservice.security.service.RespProjectMediaService;

import java.util.List;

@RestController
@RequestMapping("/api/resp-project-media")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RespProjectMediaController {


    @Autowired
    private RespProjectMediaService resProjectMediaService;

    @PostMapping("/{projectId}/upload")
//    @PreAuthorize("hasRole('APPRENANT') or hasRole('RESPONSABLE')")
    public ResponseEntity<RespProjectMediaResponse> uploadFile(
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("mediaType") MediaType mediaType) {

        RespProjectMediaResponse media = resProjectMediaService.addRespProjectMedia(projectId, file, mediaType);
        return ResponseEntity.ok(media);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<List<RespProjectMediaResponse>> getRespProjectMedia(@PathVariable Long projectId) {
        List<RespProjectMediaResponse> mediaList = resProjectMediaService.getRespProjectMediaByProject(projectId);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/{projectId}/type/{mediaType}")
    public ResponseEntity<List<RespProjectMediaResponse>> getRespProjectMediaByType(
            @PathVariable Long projectId,
            @PathVariable MediaType mediaType) {

        List<RespProjectMediaResponse> mediaList = resProjectMediaService.getRespProjectMediaByType(projectId, mediaType);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/download/{mediaId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long mediaId) {
        RespProjectMedia media = resProjectMediaService.getRespProjectMediaById(mediaId);

        byte[] fileContent = resProjectMediaService.getRespProjectMediaContent(mediaId);

        ByteArrayResource resource = new ByteArrayResource(fileContent);

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(media.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + media.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<?> deleteMedia(@PathVariable Long mediaId) {
        resProjectMediaService.deleteRespProjectMedia(mediaId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/media-types")
    public ResponseEntity<MediaType[]> getMediaTypes() {
        return ResponseEntity.ok(MediaType.values());
    }
}

