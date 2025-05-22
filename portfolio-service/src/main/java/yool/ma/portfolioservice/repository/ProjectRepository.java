package yool.ma.portfolioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yool.ma.portfolioservice.model.Project;
import yool.ma.portfolioservice.ennum.ProjectStatus;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByProfileId(Long profileId);
    List<Project> findByStatus(ProjectStatus status);
    List<Project> findByProfileIdAndStatus(Long profileId, ProjectStatus status);
}
