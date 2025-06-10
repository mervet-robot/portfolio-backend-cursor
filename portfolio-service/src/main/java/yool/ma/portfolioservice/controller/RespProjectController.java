package yool.ma.portfolioservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yool.ma.portfolioservice.dto.RespProjectRequest;
import yool.ma.portfolioservice.dto.RespProjectResponse;
import yool.ma.portfolioservice.ennum.ProjectStatus;
import yool.ma.portfolioservice.security.service.RespProjectService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/resp-projects")
@RequiredArgsConstructor
public class RespProjectController {

    private final RespProjectService respProjectService;

    @PostMapping("/responsable/{responsableId}")
    public ResponseEntity<RespProjectResponse> createRespProject(
            @PathVariable Long responsableId,
            @RequestBody RespProjectRequest request) {
        RespProjectResponse response = respProjectService.createRespProject(responsableId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/responsable/{responsableId}")
    public ResponseEntity<List<RespProjectResponse>> getAllRespProjectsByResponsableId(
            @PathVariable Long responsableId) {
        List<RespProjectResponse> responses = respProjectService.getAllRespProjectsByResponsableId(responsableId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespProjectResponse> getRespProjectById(@PathVariable Long id) {
        RespProjectResponse response = respProjectService.getRespProjectById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespProjectResponse> updateRespProject(
            @PathVariable Long id,
            @RequestBody RespProjectRequest request) {
        RespProjectResponse response = respProjectService.updateRespProject(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRespProject(@PathVariable Long id) {
        respProjectService.deleteRespProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/responsable/{responsableId}/status/{status}")
    public ResponseEntity<List<RespProjectResponse>> getRespProjectsByResponsableIdAndStatus(
            @PathVariable Long responsableId,
            @PathVariable ProjectStatus status) {
        List<RespProjectResponse> responses = respProjectService.getRespProjectsByResponsableIdAndStatus(responsableId, status);
        return ResponseEntity.ok(responses);
    }
} 