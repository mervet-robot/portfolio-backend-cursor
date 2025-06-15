package yool.ma.portfolioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yool.ma.portfolioservice.ennum.ProjectSubmitStatus;
import yool.ma.portfolioservice.model.ProjectSubmit;

import java.util.List;

public interface ProjectSubmitRepository extends JpaRepository<ProjectSubmit, Long> {

    List<ProjectSubmit> findByApprenantId(Long apprenantId);

    List<ProjectSubmit> findByStatus(ProjectSubmitStatus status);

    List<ProjectSubmit> findByApprenantIdAndStatus(Long apprenantId, ProjectSubmitStatus status);
} 