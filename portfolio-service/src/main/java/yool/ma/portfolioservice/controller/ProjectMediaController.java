package yool.ma.portfolioservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;
import yool.ma.portfolioservice.ennum.MediaType;
import yool.ma.portfolioservice.model.ProjectMedia;
import yool.ma.portfolioservice.security.service.ProjectMediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-media")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProjectMediaController {

    @Autowired
    private ProjectMediaService projectMediaService;

    @PostMapping("/{projectId}/upload")
//    @PreAuthorize("hasRole('APPRENANT') or hasRole('RESPONSABLE')")
    public ResponseEntity<ProjectMedia> uploadFile(
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("mediaType") MediaType mediaType) {

        ProjectMedia media = projectMediaService.addProjectMedia(projectId, file, mediaType);
        return ResponseEntity.ok(media);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<List<ProjectMedia>> getProjectMedia(@PathVariable Long projectId) {
        List<ProjectMedia> mediaList = projectMediaService.getProjectMediaByProject(projectId);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/{projectId}/type/{mediaType}")
    public ResponseEntity<List<ProjectMedia>> getProjectMediaByType(
            @PathVariable Long projectId,
            @PathVariable MediaType mediaType) {

        List<ProjectMedia> mediaList = projectMediaService.getProjectMediaByType(projectId, mediaType);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/download/{mediaId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long mediaId) {
        ProjectMedia media = projectMediaService.getProjectMediaByProject(mediaId).stream()
                .filter(m -> m.getId().equals(mediaId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Media not found"));

        byte[] fileContent = projectMediaService.getProjectMediaContent(mediaId);

        ByteArrayResource resource = new ByteArrayResource(fileContent);

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(media.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + media.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<?> deleteMedia(@PathVariable Long mediaId) {
        projectMediaService.deleteProjectMedia(mediaId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/media-types")
    public ResponseEntity<MediaType[]> getMediaTypes() {
        return ResponseEntity.ok(MediaType.values());
    }
}
