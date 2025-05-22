package yool.ma.portfolioservice.security.service;

import yool.ma.portfolioservice.dto.SoftSkillRequest;
import yool.ma.portfolioservice.dto.SoftSkillResponse;
import yool.ma.portfolioservice.model.SoftSkill;
import yool.ma.portfolioservice.model.Profile;
import yool.ma.portfolioservice.repository.SoftSkillRepository;
import yool.ma.portfolioservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SoftSkillService {

    private final SoftSkillRepository softSkillRepository;
    private final ProfileRepository profileRepository;

    public SoftSkillResponse createSoftSkill(Long profileId, SoftSkillRequest request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        if (softSkillRepository.existsByNameAndProfileId(request.getName(), profileId)) {
            throw new RuntimeException("Soft skill already exists for this profile");
        }

        SoftSkill softSkill = new SoftSkill();
        softSkill.setName(request.getName());
        softSkill.setProfile(profile);

        SoftSkill savedSoftSkill = softSkillRepository.save(softSkill);
        return mapToResponse(savedSoftSkill);
    }

    public List<SoftSkillResponse> getAllSoftSkillsByProfileId(Long profileId) {
        return softSkillRepository.findByProfileId(profileId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SoftSkillResponse updateSoftSkill(Long id, SoftSkillRequest request) {
        SoftSkill softSkill = softSkillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Soft skill not found with id: " + id));

        softSkill.setName(request.getName());
        SoftSkill updatedSoftSkill = softSkillRepository.save(softSkill);
        return mapToResponse(updatedSoftSkill);
    }

    public void deleteSoftSkill(Long id) {
        if (!softSkillRepository.existsById(id)) {
            throw new RuntimeException("Soft skill not found with id: " + id);
        }
        softSkillRepository.deleteById(id);
    }

    private SoftSkillResponse mapToResponse(SoftSkill softSkill) {
        SoftSkillResponse response = new SoftSkillResponse();
        response.setId(softSkill.getId());
        response.setName(softSkill.getName());
        return response;
    }
}
