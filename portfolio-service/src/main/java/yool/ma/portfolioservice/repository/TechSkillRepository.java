package yool.ma.portfolioservice.repository;

import yool.ma.portfolioservice.ennum.SkillLevel;
import yool.ma.portfolioservice.model.TechSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TechSkillRepository extends JpaRepository<TechSkill, Long> {
    List<TechSkill> findByProfileId(Long profileId);
    List<TechSkill> findByCategory(String category);
    List<TechSkill> findByLevel(SkillLevel level);
    boolean existsByNameAndProfileId(String name, Long profileId);
}