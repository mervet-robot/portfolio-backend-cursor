package yool.ma.portfolioservice.repository;

import org.springframework.stereotype.Repository;
import yool.ma.portfolioservice.ennum.MediaType;
import yool.ma.portfolioservice.model.Project;
import yool.ma.portfolioservice.model.ProjectMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface ProjectMediaRepository extends JpaRepository<ProjectMedia, Long> {
    List<ProjectMedia> findByProject(Project project);

    List<ProjectMedia> findByProjectAndMediaType(Project project, MediaType mediaType);
}