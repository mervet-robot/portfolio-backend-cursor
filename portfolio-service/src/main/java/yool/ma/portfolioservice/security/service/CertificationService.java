package yool.ma.portfolioservice.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yool.ma.portfolioservice.dto.CertificationRequest;
import yool.ma.portfolioservice.dto.CertificationResponse;
import yool.ma.portfolioservice.model.Certification;
import yool.ma.portfolioservice.model.Profile;
import yool.ma.portfolioservice.repository.CertificationRepository;
import yool.ma.portfolioservice.repository.ProfileRepository;
import org.springframework.transaction.annotation.Transactional;
import yool.ma.portfolioservice.repository.CertifMediaRepository;
import yool.ma.portfolioservice.repository.UserRepository;
import yool.ma.portfolioservice.model.CertifMedia;
import yool.ma.portfolioservice.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final ProfileRepository profileRepository;
    private final CertifMediaRepository certifMediaRepository;
    private final UserRepository userRepository;

    // Create
    public CertificationResponse createCertification(Long profileId, CertificationRequest request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        Certification certification = new Certification();
        mapRequestToCertification(request, certification);
        certification.setProfile(profile);

        Certification savedCertification = certificationRepository.save(certification);
        return mapToResponse(savedCertification);
    }

    // Create Certification from CertifMedia
    @Transactional
    public CertificationResponse createCertificationFromMedia(Long userId, Long certifMediaId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        CertifMedia certifMedia = certifMediaRepository.findById(certifMediaId)
                .orElseThrow(() -> new RuntimeException("CertifMedia not found with id: " + certifMediaId));

        if (user.getProfile() == null) {
            throw new RuntimeException("User does not have a profile yet.");
        }

        // Check if a certification for this media already exists to prevent duplicates
        // This assumes a CertifMedia can only correspond to one Certification entry in a profile.
        // You might need a more robust check or a field in Certification linking to CertifMedia if that's the case.
        // For now, let's assume one-to-one or allow duplicates and rely on frontend to prevent.

        Certification certification = new Certification();
        certification.setProfile(user.getProfile());
        certification.setName(certifMedia.getTitre());
        certification.setDescription(certifMedia.getDescription());
        certification.setIssuingOrganization(certifMedia.getCategory()); // Using category as organization
        certification.setIssueDate(certifMedia.getUploadDate() != null ? certifMedia.getUploadDate().toLocalDate() : LocalDate.now());
        certification.setExpiryDate(null); // No expiry date from CertifMedia
        certification.setCertificateUrl(certifMedia.getFilePath());
        certification.setValidationLink(certifMedia.isVerified() ? "Verified by system" : "Not verified");
        certification.setCategory(certifMedia.getCategory());
        certification.setManuallyAdded(false); // Indicates it's created from media
        certification.setCertifMediaId(certifMediaId);

        Certification savedCertification = certificationRepository.save(certification);
        return mapToResponse(savedCertification);
    }

    // Get all certifications for a profile
    public List<CertificationResponse> getAllCertificationsByProfileId(Long profileId) {
        return certificationRepository.findByProfileId(profileId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get by ID
    public CertificationResponse getCertificationById(Long id) {
        Certification certification = certificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certification not found with id: " + id));
        return mapToResponse(certification);
    }

    // Update
    @Transactional
    public CertificationResponse updateCertification(Long id, CertificationRequest request) {
        Certification certification = certificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certification not found with id: " + id));

        mapRequestToCertification(request, certification);
        Certification updatedCertification = certificationRepository.save(certification);
        return mapToResponse(updatedCertification);
    }

    // Delete
    public void deleteCertification(Long id) {
        if (!certificationRepository.existsById(id)) {
            throw new RuntimeException("Certification not found with id: " + id);
        }
        certificationRepository.deleteById(id);
    }

    // Helper methods
    private CertificationResponse mapToResponse(Certification certification) {
        CertificationResponse response = new CertificationResponse();
        response.setId(certification.getId());
        response.setName(certification.getName());
        response.setDescription(certification.getDescription());
        response.setIssuingOrganization(certification.getIssuingOrganization());
        response.setIssueDate(certification.getIssueDate());
        response.setExpiryDate(certification.getExpiryDate());
        response.setCertificateUrl(certification.getCertificateUrl());
        response.setValidationLink(certification.getValidationLink());
        response.setCategory(certification.getCategory());
        response.setManuallyAdded(certification.isManuallyAdded());
        response.setCertifMediaId(certification.getCertifMediaId());
        return response;
    }

    private void mapRequestToCertification(CertificationRequest request, Certification certification) {
        certification.setName(request.getName());
        certification.setDescription(request.getDescription());
        certification.setIssuingOrganization(request.getIssuingOrganization());
        certification.setIssueDate(request.getIssueDate());
        certification.setExpiryDate(request.getExpiryDate());
        certification.setCertificateUrl(request.getCertificateUrl());
        certification.setValidationLink(request.getValidationLink());
        certification.setCategory(request.getCategory());
        certification.setManuallyAdded(request.isManuallyAdded());
    }
}