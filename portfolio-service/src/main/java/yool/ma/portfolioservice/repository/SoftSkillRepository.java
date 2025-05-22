package yool.ma.portfolioservice.repository;

import yool.ma.portfolioservice.model.SoftSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SoftSkillRepository extends JpaRepository<SoftSkill, Long> {
    List<SoftSkill> findByProfileId(Long profileId);
    boolean existsByNameAndProfileId(String name, Long profileId);
}
