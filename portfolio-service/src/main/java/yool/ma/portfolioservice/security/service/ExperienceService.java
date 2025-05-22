package yool.ma.portfolioservice.security.service;


import yool.ma.portfolioservice.dto.ExperienceRequest;
import yool.ma.portfolioservice.dto.ExperienceResponse;
import yool.ma.portfolioservice.model.Experience;
import yool.ma.portfolioservice.model.Profile;
import yool.ma.portfolioservice.repository.ExperienceRepository;
import yool.ma.portfolioservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final ProfileRepository profileRepository;

    public ExperienceResponse createExperience(Long profileId, ExperienceRequest request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        Experience experience = new Experience();
        mapRequestToExperience(request, experience);
        experience.setProfile(profile);

        Experience savedExperience = experienceRepository.save(experience);
        return mapToResponse(savedExperience);
    }

    public List<ExperienceResponse> getAllExperiencesByProfileId(Long profileId) {
        return experienceRepository.findByProfileId(profileId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ExperienceResponse getExperienceById(Long id) {
        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Experience not found with id: " + id));
        return mapToResponse(experience);
    }

    @Transactional
    public ExperienceResponse updateExperience(Long id, ExperienceRequest request) {
        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Experience not found with id: " + id));

        mapRequestToExperience(request, experience);
        Experience updatedExperience = experienceRepository.save(experience);
        return mapToResponse(updatedExperience);
    }

    public void deleteExperience(Long id) {
        if (!experienceRepository.existsById(id)) {
            throw new RuntimeException("Experience not found with id: " + id);
        }
        experienceRepository.deleteById(id);
    }

    private ExperienceResponse mapToResponse(Experience experience) {
        ExperienceResponse response = new ExperienceResponse();
        response.setId(experience.getId());
        response.setTitle(experience.getTitle());
        response.setCompany(experience.getCompany());
        response.setLocation(experience.getLocation());
        response.setDescription(experience.getDescription());
        response.setStartDate(experience.getStartDate());
        response.setEndDate(experience.getEndDate());
        response.setCurrent(experience.isCurrent());
        response.setEmploymentType(experience.getEmploymentType());
        return response;
    }

    private void mapRequestToExperience(ExperienceRequest request, Experience experience) {
        experience.setTitle(request.getTitle());
        experience.setCompany(request.getCompany());
        experience.setLocation(request.getLocation());
        experience.setDescription(request.getDescription());
        experience.setStartDate(request.getStartDate());
        experience.setEndDate(request.getEndDate());
        experience.setCurrent(request.isCurrent());
        experience.setEmploymentType(request.getEmploymentType());
    }
}
