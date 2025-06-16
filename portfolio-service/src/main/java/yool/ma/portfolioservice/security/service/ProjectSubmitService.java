package yool.ma.portfolioservice.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yool.ma.portfolioservice.dto.ProjectSubmitRequest;
import yool.ma.portfolioservice.dto.ProjectSubmitResponse;
import yool.ma.portfolioservice.ennum.ProjectStatus;
import yool.ma.portfolioservice.ennum.ProjectSubmitStatus;
import yool.ma.portfolioservice.model.*;
import yool.ma.portfolioservice.repository.ProfileRepository;
import yool.ma.portfolioservice.repository.ProjectRepository;
import yool.ma.portfolioservice.repository.ProjectSubmitRepository;
import yool.ma.portfolioservice.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectSubmitService {

    private final ProjectSubmitRepository projectSubmitRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public ProjectSubmitResponse submitProject(Long apprenantId, ProjectSubmitRequest request) {
        User apprenant = userRepository.findById(apprenantId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + apprenantId));

        ProjectSubmit projectSubmit = new ProjectSubmit();
        mapRequestToProjectSubmit(request, projectSubmit);
        projectSubmit.setApprenant(apprenant);
        projectSubmit.setStatus(ProjectSubmitStatus.SUBMITTED); // Default status

        ProjectSubmit savedProjectSubmit = projectSubmitRepository.save(projectSubmit);
        return mapToResponse(savedProjectSubmit);
    }

    @Transactional
    public List<ProjectSubmitResponse> getSubmittedProjects() {
        return projectSubmitRepository.findByStatus(ProjectSubmitStatus.SUBMITTED)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectSubmitResponse reviewProject(Long projectId, ProjectSubmitStatus status) {
        if (status != ProjectSubmitStatus.VALIDATED && status != ProjectSubmitStatus.REJECTED) {
            throw new IllegalArgumentException("Status must be VALIDATED or REJECTED");
        }

        ProjectSubmit projectSubmit = projectSubmitRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project submission not found with id: " + projectId));

        projectSubmit.setStatus(status);

        ProjectSubmit updatedProject = projectSubmitRepository.save(projectSubmit);
        Long newProjectId = null;

        if (status == ProjectSubmitStatus.VALIDATED) {
            Project newProject = createProjectFromSubmission(updatedProject);
            newProjectId = newProject.getId();
        }

        ProjectSubmitResponse response = mapToResponse(updatedProject);
        response.setProjectId(newProjectId);
        return response;
    }

    private Project createProjectFromSubmission(ProjectSubmit projectSubmit) {
        Profile profile = profileRepository.findByUserId(projectSubmit.getApprenant().getId())
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + projectSubmit.getApprenant().getId()));

        Project project = new Project();
        project.setProfile(profile);
        project.setTitle(projectSubmit.getTitle());
        project.setDescription(projectSubmit.getDescription());
        project.setStartDate(projectSubmit.getStartDate());
        project.setEndDate(projectSubmit.getEndDate());
        project.setSkills(new HashSet<>(projectSubmit.getSkills()));
        project.setStatus(ProjectStatus.EVALUATED); // Mark as evaluated in the main portfolio

        // Copy media files
        Set<ProjectMedia> newMediaFiles = projectSubmit.getMediaFiles().stream()
                .map(submitMedia -> {
                    ProjectMedia newMedia = new ProjectMedia();
                    newMedia.setProject(project);
                    newMedia.setFileName(submitMedia.getFileName());
                    newMedia.setFileType(submitMedia.getFileType());
                    newMedia.setFilePath(submitMedia.getFilePath());
                    newMedia.setMediaType(submitMedia.getMediaType());
                    return newMedia;
                }).collect(Collectors.toSet());
        project.setMediaFiles(newMediaFiles);

        return projectRepository.save(project);
    }

    private ProjectSubmitResponse mapToResponse(ProjectSubmit projectSubmit) {
        ProjectSubmitResponse response = new ProjectSubmitResponse();
        response.setId(projectSubmit.getId());
        response.setTitle(projectSubmit.getTitle());
        response.setDescription(projectSubmit.getDescription());
        response.setStartDate(projectSubmit.getStartDate());
        response.setEndDate(projectSubmit.getEndDate());
        response.setStatus(projectSubmit.getStatus());
        response.setSkills(projectSubmit.getSkills());
        if (projectSubmit.getApprenant() != null) {
            response.setApprenantId(projectSubmit.getApprenant().getId());
        }
        return response;
    }

    private void mapRequestToProjectSubmit(ProjectSubmitRequest request, ProjectSubmit projectSubmit) {
        projectSubmit.setTitle(request.getTitle());
        projectSubmit.setDescription(request.getDescription());
        projectSubmit.setStartDate(request.getStartDate());
        projectSubmit.setEndDate(request.getEndDate());
        projectSubmit.setSkills(request.getSkills());
    }
} 