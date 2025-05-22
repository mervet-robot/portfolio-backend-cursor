package yool.ma.portfolioservice.controller;

import yool.ma.portfolioservice.dto.FeedbackRequest;
import yool.ma.portfolioservice.dto.FeedbackResponse;
import yool.ma.portfolioservice.dto.MessageResponse;
import yool.ma.portfolioservice.security.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponse> createFeedback(@RequestBody FeedbackRequest request) {
        FeedbackResponse response = feedbackService.createFeedback(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedbackByProjectId(@PathVariable Long projectId) {
        List<FeedbackResponse> responses = feedbackService.getAllFeedbackByProjectId(projectId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/reviewer/{reviewerId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedbackByReviewerId(@PathVariable Long reviewerId) {
        List<FeedbackResponse> responses = feedbackService.getAllFeedbackByReviewerId(reviewerId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponse> updateFeedback(
            @PathVariable Long id,
            @RequestBody FeedbackRequest request) {
        FeedbackResponse response = feedbackService.updateFeedback(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteFeedback(@PathVariable Long id) {
        MessageResponse response = feedbackService.deleteFeedback(id);
        return ResponseEntity.ok(response);
    }
}
