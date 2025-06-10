package yool.ma.portfolioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yool.ma.portfolioservice.ennum.ProjectStatus;
import yool.ma.portfolioservice.model.RespProject;

import java.util.List;

@Repository
public interface RespProjectRepository extends JpaRepository<RespProject, Long> {
    List<RespProject> findByResponsableId(Long responsableId);
    List<RespProject> findByResponsableIdAndStatus(Long responsableId, ProjectStatus status);
} 