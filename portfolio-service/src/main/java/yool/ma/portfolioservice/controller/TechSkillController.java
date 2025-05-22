package yool.ma.portfolioservice.controller;

import yool.ma.portfolioservice.dto.TechSkillRequest;
import yool.ma.portfolioservice.dto.TechSkillResponse;
import yool.ma.portfolioservice.security.service.TechSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profiles/{profileId}/tech-skills")
@RequiredArgsConstructor
public class TechSkillController {

    private final TechSkillService techSkillService;

    @PostMapping
    public ResponseEntity<TechSkillResponse> createTechSkill(
            @PathVariable Long profileId,
            @RequestBody TechSkillRequest request) {
        TechSkillResponse response = techSkillService.createTechSkill(profileId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TechSkillResponse>> getAllTechSkillsByProfileId(
            @PathVariable Long profileId) {
        List<TechSkillResponse> responses = techSkillService.getAllTechSkillsByProfileId(profileId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{techSkillId}")
    public ResponseEntity<TechSkillResponse> updateTechSkill(
            @PathVariable Long profileId,
            @PathVariable Long techSkillId,
            @RequestBody TechSkillRequest request) {
        TechSkillResponse response = techSkillService.updateTechSkill(techSkillId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{techSkillId}")
    public ResponseEntity<Void> deleteTechSkill(
            @PathVariable Long profileId,
            @PathVariable Long techSkillId) {
        techSkillService.deleteTechSkill(techSkillId);
        return ResponseEntity.noContent().build();
    }
}
