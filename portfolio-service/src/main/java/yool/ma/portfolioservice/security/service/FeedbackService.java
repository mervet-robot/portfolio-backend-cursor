package yool.ma.portfolioservice.security.service;

import yool.ma.portfolioservice.dto.FeedbackRequest;
import yool.ma.portfolioservice.dto.FeedbackResponse;
import yool.ma.portfolioservice.dto.MessageResponse;
import yool.ma.portfolioservice.model.Feedback;
import yool.ma.portfolioservice.model.Project;
import yool.ma.portfolioservice.model.User;
import yool.ma.portfolioservice.repository.FeedbackRepository;
import yool.ma.portfolioservice.repository.ProjectRepository;
import yool.ma.portfolioservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

//    private final ValidationService validationService;


    private final FeedbackRepository feedbackRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public FeedbackResponse createFeedback(FeedbackRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + request.getProjectId()));

//        validationService.validateScores(request.getTechnicalScore(), request.getAttitudeScore());

        User reviewer = userRepository.findById(request.getReviewerId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getReviewerId()));

        Feedback feedback = new Feedback();
        feedback.setProject(project);
        feedback.setReviewer(reviewer);
        feedback.setComment(request.getComment());
        feedback.setTechnicalScore(request.getTechnicalScore());
        feedback.setAttitudeScore(request.getAttitudeScore());

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return mapToResponse(savedFeedback);
    }

    public List<FeedbackResponse> getAllFeedbackByProjectId(Long projectId) {
        return feedbackRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<FeedbackResponse> getAllFeedbackByReviewerId(Long reviewerId) {
        return feedbackRepository.findByReviewerId(reviewerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FeedbackResponse updateFeedback(Long id, FeedbackRequest request) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));

//        validationService.validateScores(request.getTechnicalScore(), request.getAttitudeScore());

        if (request.getComment() != null) {
            feedback.setComment(request.getComment());
        }
        if (request.getTechnicalScore() != null) {
            feedback.setTechnicalScore(request.getTechnicalScore());
        }
        if (request.getAttitudeScore() != null) {
            feedback.setAttitudeScore(request.getAttitudeScore());
        }

        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return mapToResponse(updatedFeedback);
    }

    public MessageResponse deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new RuntimeException("Feedback not found with id: " + id);
        }
        feedbackRepository.deleteById(id);
        return new MessageResponse("Feedback deleted successfully");
    }

    private FeedbackResponse mapToResponse(Feedback feedback) {
        FeedbackResponse response = new FeedbackResponse();
        response.setId(feedback.getId());
        response.setProjectId(feedback.getProject().getId());
        response.setReviewerId(feedback.getReviewer().getId());
        response.setComment(feedback.getComment());
        response.setTechnicalScore(feedback.getTechnicalScore());
        response.setAttitudeScore(feedback.getAttitudeScore());
        response.setCreatedAt(feedback.getCreatedAt());
        return response;
    }
}
