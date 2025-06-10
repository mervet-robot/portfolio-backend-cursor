package yool.ma.portfolioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yool.ma.portfolioservice.ennum.MediaType;
import yool.ma.portfolioservice.model.Project;
import yool.ma.portfolioservice.model.ProjectMedia;
import yool.ma.portfolioservice.model.RespProject;
import yool.ma.portfolioservice.model.RespProjectMedia;

import java.util.List;

public interface RespProjectMediaRepository extends JpaRepository<RespProjectMedia, Long> {
    List<RespProjectMedia> findByRespProject(RespProject respProject);

    List<RespProjectMedia> findByRespProjectAndMediaType(RespProject respProject, MediaType mediaType);
}