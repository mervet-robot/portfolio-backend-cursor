package yool.ma.portfolioservice.controller;

import yool.ma.portfolioservice.dto.ExperienceRequest;
import yool.ma.portfolioservice.dto.ExperienceResponse;
import yool.ma.portfolioservice.security.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profiles/{profileId}/experiences")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @PostMapping
    public ResponseEntity<ExperienceResponse> createExperience(
            @PathVariable Long profileId,
            @RequestBody ExperienceRequest request) {
        ExperienceResponse response = experienceService.createExperience(profileId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExperienceResponse>> getAllExperiencesByProfileId(
            @PathVariable Long profileId) {
        List<ExperienceResponse> responses = experienceService.getAllExperiencesByProfileId(profileId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{experienceId}")
    public ResponseEntity<ExperienceResponse> getExperienceById(
            @PathVariable Long profileId,
            @PathVariable Long experienceId) {
        ExperienceResponse response = experienceService.getExperienceById(experienceId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{experienceId}")
    public ResponseEntity<ExperienceResponse> updateExperience(
            @PathVariable Long profileId,
            @PathVariable Long experienceId,
            @RequestBody ExperienceRequest request) {
        ExperienceResponse response = experienceService.updateExperience(experienceId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{experienceId}")
    public ResponseEntity<Void> deleteExperience(
            @PathVariable Long profileId,
            @PathVariable Long experienceId) {
        experienceService.deleteExperience(experienceId);
        return ResponseEntity.noContent().build();
    }
}
