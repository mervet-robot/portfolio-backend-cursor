package yool.ma.portfolioservice.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yool.ma.portfolioservice.dto.ProjectRequest;
import yool.ma.portfolioservice.dto.ProjectResponse;
import yool.ma.portfolioservice.model.Profile;
import yool.ma.portfolioservice.model.Project;
import yool.ma.portfolioservice.ennum.ProjectStatus;
import yool.ma.portfolioservice.repository.ProfileRepository;
import yool.ma.portfolioservice.repository.ProjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;

    // Create a new project
    public ProjectResponse createProject(Long profileId, ProjectRequest request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        Project project = new Project();
        mapRequestToProject(request, project);
        project.setProfile(profile);

        Project savedProject = projectRepository.save(project);
        return mapToResponse(savedProject);
    }

    // Get all projects for a profile
    public List<ProjectResponse> getAllProjectsByProfileId(Long profileId) {
        return projectRepository.findByProfileId(profileId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get project by ID
    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        return mapToResponse(project);
    }

    // Update project
    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        mapRequestToProject(request, project);
        Project updatedProject = projectRepository.save(project);
        return mapToResponse(updatedProject);
    }

    // Delete project
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    // Get projects by status for a profile
    public List<ProjectResponse> getProjectsByProfileIdAndStatus(Long profileId, ProjectStatus status) {
        return projectRepository.findByProfileIdAndStatus(profileId, status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Helper methods
    private ProjectResponse mapToResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setTitle(project.getTitle());
        response.setDescription(project.getDescription());
        response.setStartDate(project.getStartDate());
        response.setEndDate(project.getEndDate());
        response.setStatus(project.getStatus());
        response.setSkills(project.getSkills());
        return response;
    }

    private void mapRequestToProject(ProjectRequest request, Project project) {
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setStatus(request.getStatus());
        project.setSkills(request.getSkills());
    }
}