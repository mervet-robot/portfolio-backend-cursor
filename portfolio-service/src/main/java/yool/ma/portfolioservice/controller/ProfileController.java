package yool.ma.portfolioservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import yool.ma.portfolioservice.dto.ProfileUpdateRequest;
import yool.ma.portfolioservice.dto.SocialLinkRequestDTO;
import yool.ma.portfolioservice.dto.SocialLinkResponseDTO;
import yool.ma.portfolioservice.model.Profile;
import yool.ma.portfolioservice.security.service.ProfileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private static final String UPLOAD_DIR = "uploads/profile-pictures/";

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Profile> getProfile(@PathVariable Long userId) {
        Profile profile = profileService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Profile> updateProfile(
            @PathVariable Long userId,
            @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        Profile updatedProfile = profileService.updateProfile(userId, profileUpdateRequest);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/{userId}/picture")
    public ResponseEntity<Profile> uploadProfilePicture(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {

        try {
            // Define the upload directory
            String uploadDir = System.getProperty("user.dir") + "/uploads/profile-pictures/";

            // Create upload directory if it doesn't exist
            Files.createDirectories(Paths.get(uploadDir));

            // Generate unique filename
            String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = Paths.get(uploadDir + fileName);

            // Save file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // The URL path that will be used by the frontend to access the image
            String webPath = "/uploads/profile-pictures/" + fileName;

            Profile updatedProfile = profileService.updateProfilePicture(userId, webPath);
            return ResponseEntity.ok(updatedProfile);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }

    // --- SocialLink Endpoints ---

    @GetMapping("/{userId}/social-links")
    public ResponseEntity<List<SocialLinkResponseDTO>> getSocialLinks(@PathVariable Long userId) {
        List<SocialLinkResponseDTO> socialLinks = profileService.getSocialLinks(userId);
        return ResponseEntity.ok(socialLinks);
    }

    @PostMapping("/{userId}/social-links")
    public ResponseEntity<SocialLinkResponseDTO> addSocialLink(
            @PathVariable Long userId,
            @RequestBody SocialLinkRequestDTO socialLinkDto) {
        SocialLinkResponseDTO newSocialLink = profileService.addSocialLink(userId, socialLinkDto);
        return new ResponseEntity<>(newSocialLink, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/social-links/{socialLinkId}")
    public ResponseEntity<SocialLinkResponseDTO> updateSocialLink(
            @PathVariable Long userId,
            @PathVariable Long socialLinkId,
            @RequestBody SocialLinkRequestDTO socialLinkDto) {
        SocialLinkResponseDTO updatedSocialLink = profileService.updateSocialLink(userId, socialLinkId, socialLinkDto);
        return ResponseEntity.ok(updatedSocialLink);
    }

    @DeleteMapping("/{userId}/social-links/{socialLinkId}")
    public ResponseEntity<Void> deleteSocialLink(
            @PathVariable Long userId,
            @PathVariable Long socialLinkId) {
        profileService.deleteSocialLink(userId, socialLinkId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bio-check")
    public ResponseEntity<String> verifyAndCorrectBio(@RequestBody Map<String, String> request) {
        String bio = request.get("bio");

        String prompt = "Correct the grammar and spelling of the following biography. Return only the corrected version, without adding new content or changing the meaning. The corrected bio must be between 20 and 50 words.\n\nBiography: " + bio;



//        String prompt = "Correct the grammar and spelling of my biography in 20 to 50 words): \"" + bio;
//                + "\". Return only the corrected version of the same bio. Maintain the word count between 20-50 words. "
//                + "Do not change the meaning or add any new content. If the text makes no sense, respond with: INVALID BIO.";

//        String prompt = "Correct the grammar and spelling of this biography: \"" + bio ;
//                + "\". Return only the corrected version of the same bio. Do not change the meaning or add any new content. If the text makes no sense, respond with: INVALID BIO.";

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:11434/api/generate";

        Map<String, Object> body = Map.of(
                "model", "tinyllama",
                "prompt", prompt,
                "stream", false
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        String responseText = ((String) response.getBody().get("response")).trim();
        if (responseText.startsWith("\"") && responseText.endsWith("\"")) {
            responseText = responseText.substring(1, responseText.length() - 1);
        }
        return ResponseEntity.ok(responseText);


//        return ResponseEntity.ok((String) response.getBody().get("response"));
    }



}