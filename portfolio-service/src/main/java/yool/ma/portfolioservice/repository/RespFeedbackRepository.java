package yool.ma.portfolioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yool.ma.portfolioservice.model.RespFeedback;

import java.util.List;

@Repository
public interface RespFeedbackRepository extends JpaRepository<RespFeedback, Long> {
    List<RespFeedback> findByRespProjectId(Long respProjectId);
    List<RespFeedback> findByReviewerId(Long reviewerId);
} 