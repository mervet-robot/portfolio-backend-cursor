package yool.ma.portfolioservice.security.service;

import yool.ma.portfolioservice.dto.TechSkillRequest;
import yool.ma.portfolioservice.dto.TechSkillResponse;
import yool.ma.portfolioservice.model.TechSkill;
import yool.ma.portfolioservice.model.Profile;
import yool.ma.portfolioservice.repository.TechSkillRepository;
import yool.ma.portfolioservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechSkillService {

    private final TechSkillRepository techSkillRepository;
    private final ProfileRepository profileRepository;

    public TechSkillResponse createTechSkill(Long profileId, TechSkillRequest request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        if (techSkillRepository.existsByNameAndProfileId(request.getName(), profileId)) {
            throw new RuntimeException("Tech skill already exists for this profile");
        }

        TechSkill techSkill = new TechSkill();
        mapRequestToTechSkill(request, techSkill);
        techSkill.setProfile(profile);

        TechSkill savedTechSkill = techSkillRepository.save(techSkill);
        return mapToResponse(savedTechSkill);
    }

    public List<TechSkillResponse> getAllTechSkillsByProfileId(Long profileId) {
        return techSkillRepository.findByProfileId(profileId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TechSkillResponse updateTechSkill(Long id, TechSkillRequest request) {
        TechSkill techSkill = techSkillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tech skill not found with id: " + id));

        mapRequestToTechSkill(request, techSkill);
        TechSkill updatedTechSkill = techSkillRepository.save(techSkill);
        return mapToResponse(updatedTechSkill);
    }

    public void deleteTechSkill(Long id) {
        if (!techSkillRepository.existsById(id)) {
            throw new RuntimeException("Tech skill not found with id: " + id);
        }
        techSkillRepository.deleteById(id);
    }

    private TechSkillResponse mapToResponse(TechSkill techSkill) {
        TechSkillResponse response = new TechSkillResponse();
        response.setId(techSkill.getId());
        response.setName(techSkill.getName());
        response.setLevel(techSkill.getLevel());
        response.setCategory(techSkill.getCategory());
        response.setYearsOfExperience(techSkill.getYearsOfExperience());
        response.setVerified(techSkill.isVerified());
        return response;
    }

    private void mapRequestToTechSkill(TechSkillRequest request, TechSkill techSkill) {
        techSkill.setName(request.getName());
        techSkill.setLevel(request.getLevel());
        techSkill.setCategory(request.getCategory());
        techSkill.setYearsOfExperience(request.getYearsOfExperience());
        techSkill.setVerified(request.isVerified());
    }
}
