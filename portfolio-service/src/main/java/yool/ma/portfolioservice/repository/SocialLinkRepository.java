package yool.ma.portfolioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yool.ma.portfolioservice.model.SocialLink;

import java.util.List;

@Repository
public interface SocialLinkRepository extends JpaRepository<SocialLink, Long> {
    List<SocialLink> findByProfileId(Long profileId);
    // You can add custom query methods here if needed later
} 