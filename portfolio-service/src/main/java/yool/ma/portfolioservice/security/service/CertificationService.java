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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final ProfileRepository profileRepository;

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