package yool.ma.portfolioservice.repository;

import yool.ma.portfolioservice.model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FormationRepository extends JpaRepository<Formation, Long> {
    List<Formation> findByProfileId(Long profileId);
    List<Formation> findByInstitution(String institution);
    List<Formation> findByDegreeContainingIgnoreCase(String degree);
}
