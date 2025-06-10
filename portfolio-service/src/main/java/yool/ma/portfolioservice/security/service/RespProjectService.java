package yool.ma.portfolioservice.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yool.ma.portfolioservice.dto.RespProjectRequest;
import yool.ma.portfolioservice.dto.RespProjectResponse;
import yool.ma.portfolioservice.ennum.ProjectStatus;
import yool.ma.portfolioservice.model.RespProject;
import yool.ma.portfolioservice.model.User;
import yool.ma.portfolioservice.repository.RespProjectRepository;
import yool.ma.portfolioservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RespProjectService {

    private final RespProjectRepository respProjectRepository;
    private final UserRepository userRepository;

    public RespProjectResponse createRespProject(Long responsableId, RespProjectRequest request) {
        User responsable = userRepository.findById(responsableId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + responsableId));

        RespProject respProject = new RespProject();
        mapRequestToRespProject(request, respProject);
        respProject.setResponsable(responsable);

        RespProject savedRespProject = respProjectRepository.save(respProject);
        return mapToResponse(savedRespProject);
    }

    public List<RespProjectResponse> getAllRespProjectsByResponsableId(Long responsableId) {
        return respProjectRepository.findByResponsableId(responsableId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RespProjectResponse getRespProjectById(Long id) {
        RespProject respProject = respProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RespProject not found with id: " + id));
        return mapToResponse(respProject);
    }

    @Transactional
    public RespProjectResponse updateRespProject(Long id, RespProjectRequest request) {
        RespProject respProject = respProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RespProject not found with id: " + id));

        mapRequestToRespProject(request, respProject);
        RespProject updatedRespProject = respProjectRepository.save(respProject);
        return mapToResponse(updatedRespProject);
    }

    public void deleteRespProject(Long id) {
        if (!respProjectRepository.existsById(id)) {
            throw new RuntimeException("RespProject not found with id: " + id);
        }
        respProjectRepository.deleteById(id);
    }

    public List<RespProjectResponse> getRespProjectsByResponsableIdAndStatus(Long responsableId, ProjectStatus status) {
        return respProjectRepository.findByResponsableIdAndStatus(responsableId, status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private RespProjectResponse mapToResponse(RespProject respProject) {
        RespProjectResponse response = new RespProjectResponse();
        response.setId(respProject.getId());
        response.setTitle(respProject.getTitle());
        response.setDescription(respProject.getDescription());
        response.setStartDate(respProject.getStartDate());
        response.setEndDate(respProject.getEndDate());
        response.setStatus(respProject.getStatus());
        response.setSkills(respProject.getSkills());
        if (respProject.getResponsable() != null) {
            response.setResponsableId(respProject.getResponsable().getId());
        }
        return response;
    }

    private void mapRequestToRespProject(RespProjectRequest request, RespProject respProject) {
        respProject.setTitle(request.getTitle());
        respProject.setDescription(request.getDescription());
        respProject.setStartDate(request.getStartDate());
        respProject.setEndDate(request.getEndDate());
        respProject.setStatus(request.getStatus());
        respProject.setSkills(request.getSkills());
    }
} 