//package yool.ma.portfolioservice.model;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "project_submit_feedbacks")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class ProjectSubmitFeedback {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "project_submit_id")
//    @JsonBackReference
//    private ProjectSubmit projectSubmit;
//
//    @Column(columnDefinition = "TEXT", nullable = false)
//    private String comment;
//
//    private Integer technicalScore;
//    private Integer attitudeScore;
//
//    private LocalDateTime createdAt;
//
//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//    }
//}
