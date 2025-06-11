package yool.ma.portfolioservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yool.ma.portfolioservice.dto.RespFeedbackRequest;
import yool.ma.portfolioservice.dto.RespFeedbackResponse;
import yool.ma.portfolioservice.security.service.RespFeedbackService;

import java.util.List;

@RestController
@RequestMapping("/api/resp-feedback")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class RespFeedbackController {

    private final RespFeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<RespFeedbackResponse> createFeedback(@RequestBody RespFeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.createFeedback(request));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<RespFeedbackResponse>> getFeedbackByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(feedbackService.getAllFeedbackByRespProjectId(projectId));
    }

    @GetMapping("/reviewer/{reviewerId}")
    public ResponseEntity<List<RespFeedbackResponse>> getFeedbackByReviewerId(@PathVariable Long reviewerId) {
        return ResponseEntity.ok(feedbackService.getAllFeedbackByReviewerId(reviewerId));
    }

    @PutMapping("/{feedbackId}")
    public ResponseEntity<RespFeedbackResponse> updateFeedback(
            @PathVariable Long feedbackId,
            @RequestBody RespFeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.updateFeedback(feedbackId, request));
    }

    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.noContent().build();
    }
}