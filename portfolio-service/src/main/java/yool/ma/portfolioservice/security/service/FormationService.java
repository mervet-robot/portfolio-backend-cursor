package yool.ma.portfolioservice.security.service;


import yool.ma.portfolioservice.dto.FormationRequest;
import yool.ma.portfolioservice.dto.FormationResponse;
import yool.ma.portfolioservice.model.Formation;
import yool.ma.portfolioservice.model.Profile;
import yool.ma.portfolioservice.repository.FormationRepository;
import yool.ma.portfolioservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormationService {

    private final FormationRepository formationRepository;
    private final ProfileRepository profileRepository;

    public FormationResponse createFormation(Long profileId, FormationRequest request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        Formation formation = new Formation();
        mapRequestToFormation(request, formation);
        formation.setProfile(profile);

        Formation savedFormation = formationRepository.save(formation);
        return mapToResponse(savedFormation);
    }

    public List<FormationResponse> getAllFormationsByProfileId(Long profileId) {
        return formationRepository.findByProfileId(profileId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public FormationResponse getFormationById(Long id) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation not found with id: " + id));
        return mapToResponse(formation);
    }

    @Transactional
    public FormationResponse updateFormation(Long id, FormationRequest request) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation not found with id: " + id));

        mapRequestToFormation(request, formation);
        Formation updatedFormation = formationRepository.save(formation);
        return mapToResponse(updatedFormation);
    }

    public void deleteFormation(Long id) {
        if (!formationRepository.existsById(id)) {
            throw new RuntimeException("Formation not found with id: " + id);
        }
        formationRepository.deleteById(id);
    }

    private FormationResponse mapToResponse(Formation formation) {
        FormationResponse response = new FormationResponse();
        response.setId(formation.getId());
        response.setDegree(formation.getDegree());
        response.setInstitution(formation.getInstitution());
        response.setFieldOfStudy(formation.getFieldOfStudy());
        response.setLocation(formation.getLocation());
        response.setDescription(formation.getDescription());
        response.setStartDate(formation.getStartDate());
        response.setEndDate(formation.getEndDate());
        response.setCurrent(formation.isCurrent());
        response.setGrade(formation.getGrade());
        response.setActivities(formation.getActivities());
        response.setDiplomaUrl(formation.getDiplomaUrl());
        return response;
    }

    private void mapRequestToFormation(FormationRequest request, Formation formation) {
        formation.setDegree(request.getDegree());
        formation.setInstitution(request.getInstitution());
        formation.setFieldOfStudy(request.getFieldOfStudy());
        formation.setLocation(request.getLocation());
        formation.setDescription(request.getDescription());
        formation.setStartDate(request.getStartDate());
        formation.setEndDate(request.getEndDate());
        formation.setCurrent(request.isCurrent());
        formation.setGrade(request.getGrade());
        formation.setActivities(request.getActivities());
        formation.setDiplomaUrl(request.getDiplomaUrl());
    }
}
