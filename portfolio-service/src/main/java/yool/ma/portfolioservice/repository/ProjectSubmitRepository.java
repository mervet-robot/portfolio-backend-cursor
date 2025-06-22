package yool.ma.portfolioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yool.ma.portfolioservice.ennum.ProjectSubmitStatus;
import yool.ma.portfolioservice.model.ProjectSubmit;

import java.util.List;

public interface ProjectSubmitRepository extends JpaRepository<ProjectSubmit, Long> {

    List<ProjectSubmit> findByProfileId(Long profileId);

    List<ProjectSubmit> findByStatus(ProjectSubmitStatus status);

    List<ProjectSubmit> findByProfileIdAndStatus(Long profileId, ProjectSubmitStatus status);

    List<ProjectSubmit> findByStatusNot(ProjectSubmitStatus status);
} 