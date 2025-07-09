package yool.ma.portfolioservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yool.ma.portfolioservice.dto.CertificationRequest;
import yool.ma.portfolioservice.dto.CertificationResponse;
import yool.ma.portfolioservice.security.service.CertificationService;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping("/profile/{profileId}")
    public ResponseEntity<CertificationResponse> createCertification(
            @PathVariable Long profileId,
            @RequestBody CertificationRequest request) {
        CertificationResponse response = certificationService.createCertification(profileId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<CertificationResponse>> getAllCertificationsByProfileId(
            @PathVariable Long profileId) {
        List<CertificationResponse> responses = certificationService.getAllCertificationsByProfileId(profileId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificationResponse> getCertificationById(@PathVariable Long id) {
        CertificationResponse response = certificationService.getCertificationById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificationResponse> updateCertification(
            @PathVariable Long id,
            @RequestBody CertificationRequest request) {
        CertificationResponse response = certificationService.updateCertification(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertification(@PathVariable Long id) {
        certificationService.deleteCertification(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/from-media/user/{userId}/media/{certifMediaId}")
    public ResponseEntity<CertificationResponse> createCertificationFromMedia(
            @PathVariable Long userId,
            @PathVariable Long certifMediaId) {
        CertificationResponse response = certificationService.createCertificationFromMedia(userId, certifMediaId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
