package yool.ma.portfolioservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yool.ma.portfolioservice.dto.ProjectRequest;
import yool.ma.portfolioservice.dto.ProjectResponse;
import yool.ma.portfolioservice.ennum.ProjectStatus;
import yool.ma.portfolioservice.security.service.ProjectService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/profile/{profileId}")
    public ResponseEntity<ProjectResponse> createProject(
            @PathVariable Long profileId,
            @RequestBody ProjectRequest request) {
        ProjectResponse response = projectService.createProject(profileId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<ProjectResponse>> getAllProjectsByProfileId(
            @PathVariable Long profileId) {
        List<ProjectResponse> responses = projectService.getAllProjectsByProfileId(profileId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        ProjectResponse response = projectService.getProjectById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectRequest request) {
        ProjectResponse response = projectService.updateProject(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile/{profileId}/status/{status}")
    public ResponseEntity<List<ProjectResponse>> getProjectsByProfileIdAndStatus(
            @PathVariable Long profileId,
            @PathVariable ProjectStatus status) {
        List<ProjectResponse> responses = projectService.getProjectsByProfileIdAndStatus(profileId, status);
        return ResponseEntity.ok(responses);
    }
}
