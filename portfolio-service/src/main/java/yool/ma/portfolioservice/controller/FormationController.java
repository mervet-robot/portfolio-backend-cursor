package yool.ma.portfolioservice.controller;


import yool.ma.portfolioservice.dto.FormationRequest;
import yool.ma.portfolioservice.dto.FormationResponse;
import yool.ma.portfolioservice.security.service.FormationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profiles/{profileId}/formations")
@RequiredArgsConstructor
public class FormationController {

    private final FormationService formationService;

    @PostMapping
    public ResponseEntity<FormationResponse> createFormation(
            @PathVariable Long profileId,
            @RequestBody FormationRequest request) {
        FormationResponse response = formationService.createFormation(profileId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FormationResponse>> getAllFormationsByProfileId(
            @PathVariable Long profileId) {
        List<FormationResponse> responses = formationService.getAllFormationsByProfileId(profileId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{formationId}")
    public ResponseEntity<FormationResponse> getFormationById(
            @PathVariable Long profileId,
            @PathVariable Long formationId) {
        FormationResponse response = formationService.getFormationById(formationId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{formationId}")
    public ResponseEntity<FormationResponse> updateFormation(
            @PathVariable Long profileId,
            @PathVariable Long formationId,
            @RequestBody FormationRequest request) {
        FormationResponse response = formationService.updateFormation(formationId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{formationId}")
    public ResponseEntity<Void> deleteFormation(
            @PathVariable Long profileId,
            @PathVariable Long formationId) {
        formationService.deleteFormation(formationId);
        return ResponseEntity.noContent().build();
    }
}