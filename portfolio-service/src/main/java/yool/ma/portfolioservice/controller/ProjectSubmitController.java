package yool.ma.portfolioservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yool.ma.portfolioservice.dto.ProjectSubmitRequest;
import yool.ma.portfolioservice.dto.ProjectSubmitResponse;
import yool.ma.portfolioservice.dto.ProjectReviewRequest;
import yool.ma.portfolioservice.security.service.ProjectSubmitService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/project-submits")
@RequiredArgsConstructor
public class ProjectSubmitController {

    private final ProjectSubmitService projectSubmitService;

    @PostMapping("/apprenant/{apprenantId}")
    public ResponseEntity<ProjectSubmitResponse> submitProject(
            @PathVariable Long apprenantId,
            @RequestBody ProjectSubmitRequest request) {
        ProjectSubmitResponse response = projectSubmitService.submitProject(apprenantId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/submitted")
    public ResponseEntity<List<ProjectSubmitResponse>> getSubmittedProjects() {
        List<ProjectSubmitResponse> responses = projectSubmitService.getSubmittedProjects();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{projectId}/review")
    public ResponseEntity<ProjectSubmitResponse> reviewProject(
            @PathVariable Long projectId,
            @RequestBody ProjectReviewRequest reviewRequest) {
        ProjectSubmitResponse response = projectSubmitService.reviewProject(projectId, reviewRequest.getStatus());
        return ResponseEntity.ok(response);
    }
} 