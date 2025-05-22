package yool.ma.portfolioservice.controller;


import yool.ma.portfolioservice.dto.LanguageRequest;
import yool.ma.portfolioservice.dto.LanguageResponse;
import yool.ma.portfolioservice.security.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profiles/{profileId}/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping
    public ResponseEntity<LanguageResponse> createLanguage(
            @PathVariable Long profileId,
            @RequestBody LanguageRequest request) {
        LanguageResponse response = languageService.createLanguage(profileId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LanguageResponse>> getAllLanguagesByProfileId(
            @PathVariable Long profileId) {
        List<LanguageResponse> responses = languageService.getAllLanguagesByProfileId(profileId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{languageId}")
    public ResponseEntity<LanguageResponse> getLanguageById(
            @PathVariable Long profileId,
            @PathVariable Long languageId) {
        LanguageResponse response = languageService.getLanguageById(languageId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{languageId}")
    public ResponseEntity<LanguageResponse> updateLanguage(
            @PathVariable Long profileId,
            @PathVariable Long languageId,
            @RequestBody LanguageRequest request) {
        LanguageResponse response = languageService.updateLanguage(languageId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{languageId}")
    public ResponseEntity<Void> deleteLanguage(
            @PathVariable Long profileId,
            @PathVariable Long languageId) {
        languageService.deleteLanguage(languageId);
        return ResponseEntity.noContent().build();
    }
}
