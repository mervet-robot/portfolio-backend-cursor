package yool.ma.portfolioservice.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yool.ma.portfolioservice.dto.MessageResponse;
import yool.ma.portfolioservice.dto.RespFeedbackRequest;
import yool.ma.portfolioservice.dto.RespFeedbackResponse;
import yool.ma.portfolioservice.model.RespFeedback;
import yool.ma.portfolioservice.model.RespProject;
import yool.ma.portfolioservice.model.User;
import yool.ma.portfolioservice.repository.RespFeedbackRepository;
import yool.ma.portfolioservice.repository.RespProjectRepository;
import yool.ma.portfolioservice.repository.UserRepository;
import yool.ma.portfolioservice.security.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RespFeedbackService {

    private final RespFeedbackRepository respFeedbackRepository;
    private final RespProjectRepository respProjectRepository;
    private final UserRepository userRepository;

    public RespFeedbackResponse createFeedback(RespFeedbackRequest request) {
        RespProject respProject = respProjectRepository.findById(request.getRespProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User reviewer = userRepository.findById(request.getReviewerId())
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found"));

        RespFeedback feedback = new RespFeedback();
        feedback.setRespProject(respProject);
        feedback.setReviewer(reviewer);
        feedback.setComment(request.getComment());
        feedback.setTechnicalScore(request.getTechnicalScore());
        feedback.setAttitudeScore(request.getAttitudeScore());

        RespFeedback savedFeedback = respFeedbackRepository.save(feedback);
        return mapToResponse(savedFeedback);
    }

    public List<RespFeedbackResponse> getAllFeedbackByRespProjectId(Long respProjectId) {
        return respFeedbackRepository.findByRespProjectId(respProjectId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RespFeedbackResponse> getAllFeedbackByReviewerId(Long reviewerId) {
        return respFeedbackRepository.findByReviewerId(reviewerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RespFeedbackResponse updateFeedback(Long id, RespFeedbackRequest request) {
        RespFeedback feedback = respFeedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));

        if (request.getComment() != null) {
            feedback.setComment(request.getComment());
        }
        if (request.getTechnicalScore() != null) {
            feedback.setTechnicalScore(request.getTechnicalScore());
        }
        if (request.getAttitudeScore() != null) {
            feedback.setAttitudeScore(request.getAttitudeScore());
        }

        RespFeedback updatedFeedback = respFeedbackRepository.save(feedback);
        return mapToResponse(updatedFeedback);
    }

    public MessageResponse deleteFeedback(Long id) {
        if (!respFeedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feedback not found");
        }
        respFeedbackRepository.deleteById(id);
        return new MessageResponse("Feedback deleted successfully");
    }

    private RespFeedbackResponse mapToResponse(RespFeedback feedback) {
        RespFeedbackResponse response = new RespFeedbackResponse();
        response.setId(feedback.getId());
        response.setRespProjectId(feedback.getRespProject().getId());
        response.setReviewerId(feedback.getReviewer().getId());
        response.setComment(feedback.getComment());
        response.setTechnicalScore(feedback.getTechnicalScore());
        response.setAttitudeScore(feedback.getAttitudeScore());
        response.setCreatedAt(feedback.getCreatedAt());
        return response;
    }
} 