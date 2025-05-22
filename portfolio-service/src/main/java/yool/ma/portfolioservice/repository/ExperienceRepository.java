package yool.ma.portfolioservice.repository;


import yool.ma.portfolioservice.ennum.EmploymentType;
import yool.ma.portfolioservice.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByProfileId(Long profileId);
    List<Experience> findByCompany(String company);
    List<Experience> findByProfileIdAndEmploymentType(Long profileId, EmploymentType employmentType);
}
