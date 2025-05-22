package yool.ma.portfolioservice.controller;

import yool.ma.portfolioservice.dto.SoftSkillRequest;
import yool.ma.portfolioservice.dto.SoftSkillResponse;
import yool.ma.portfolioservice.security.service.SoftSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profiles/{profileId}/soft-skills")
@RequiredArgsConstructor
public class SoftSkillController {

    private final SoftSkillService softSkillService;

    @PostMapping
    public ResponseEntity<SoftSkillResponse> createSoftSkill(
            @PathVariable Long profileId,
            @RequestBody SoftSkillRequest request) {
        SoftSkillResponse response = softSkillService.createSoftSkill(profileId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SoftSkillResponse>> getAllSoftSkillsByProfileId(
            @PathVariable Long profileId) {
        List<SoftSkillResponse> responses = softSkillService.getAllSoftSkillsByProfileId(profileId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{softSkillId}")
    public ResponseEntity<SoftSkillResponse> updateSoftSkill(
            @PathVariable Long profileId,
            @PathVariable Long softSkillId,
            @RequestBody SoftSkillRequest request) {
        SoftSkillResponse response = softSkillService.updateSoftSkill(softSkillId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{softSkillId}")
    public ResponseEntity<Void> deleteSoftSkill(
            @PathVariable Long profileId,
            @PathVariable Long softSkillId) {
        softSkillService.deleteSoftSkill(softSkillId);
        return ResponseEntity.noContent().build();
    }
}
