package yool.ma.portfolioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yool.ma.portfolioservice.ennum.MediaType;
import yool.ma.portfolioservice.model.CertifMedia;
import yool.ma.portfolioservice.model.User;

import java.util.List;

@Repository
public interface CertifMediaRepository extends JpaRepository<CertifMedia, Long> {
    List<CertifMedia> findByUser(User user);
    List<CertifMedia> findByUserAndMediaType(User user, MediaType mediaType);
    List<CertifMedia> findByUserAndCategory(User user, String category);
    List<CertifMedia> findByUserAndMediaTypeAndCategory(User user, MediaType mediaType, String category);
    List<CertifMedia> findByUserAndTitreContainingIgnoreCase(User user, String titre);

} 