package com.example.giffunding.controller;

import com.example.giffunding.dto.request.CreateReviewRequestDto;
import com.example.giffunding.dto.request.UpdateReviewRequestDto;
import com.example.giffunding.dto.response.ReviewResponseDto;
import com.example.giffunding.entity.Review;
import com.example.giffunding.entity.User;
import com.example.giffunding.repository.ReviewRepository;
import com.example.giffunding.repository.UserRepository;
import com.example.giffunding.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final UserRepository userRepository;

    //리뷰 생성
    @PostMapping("")
    public ResponseEntity<Long> createReview(@RequestBody CreateReviewRequestDto createReviewRequestDto) {
        Optional<User> existedUser =  userRepository.findById(createReviewRequestDto.getUserId());
        if (existedUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Review review = Review.builder()
                .user(existedUser.get())
                .text(createReviewRequestDto.getText())
                .build();

        return ResponseEntity.ok(reviewService.saveReview(review));
    }

    //특정 사용자 리뷰 가져오기
    @GetMapping("")
    public ResponseEntity<ReviewResponseDto> getReview(@RequestParam("review_id") Long id){
        Optional<Review> review = reviewService.getReview(id);
        if (review.isPresent()) {
            ReviewResponseDto reviewDTO = ReviewResponseDto.builder()
                    .userId(review.get().getUser().getId())
                    .text(review.get().getText())
                    .build();
            return ResponseEntity.ok(reviewDTO);
        }
        return ResponseEntity.notFound().build();
    }

    //특정 리뷰 업데이트
    @PutMapping("")
    public ResponseEntity<Long> updateReview(@RequestParam("review_id") Long id, @RequestBody UpdateReviewRequestDto updateReviewRequestDto) {
        Optional<Review> savedReview = reviewService.getReview(id);
        if (savedReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        reviewService.updateReview(savedReview.get(), updateReviewRequestDto);
        return ResponseEntity.ok(id);
    }

    //특정 리뷰 삭제
    @DeleteMapping("")
    public ResponseEntity<Long> deleteReview(@RequestParam("review_id") Long id) {
        Optional<Review> savedReview = reviewService.getReview(id);
        if (savedReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Long deletedReviewId = reviewService.deleteReview(id);
        return ResponseEntity.ok(deletedReviewId);
    }

}
