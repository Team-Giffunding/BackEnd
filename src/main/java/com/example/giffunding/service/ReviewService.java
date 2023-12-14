package com.example.giffunding.service;

import com.example.giffunding.dto.request.UpdateReviewRequestDto;
import com.example.giffunding.entity.Review;
import com.example.giffunding.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional
    public Long saveReview(Review review) {
        reviewRepository.save(review);
        return review.getId();
    }
    public Optional<Review> getReview(Long review_id){
        return reviewRepository.findById(review_id);
    }

    @Transactional
    public void updateReview(Review existedReview, UpdateReviewRequestDto updateReview) {
        existedReview.updateReview(updateReview.getText());
    }
    @Transactional
    public Long deleteReview(Long id) {
        reviewRepository.deleteById(id);
        return id;
    }
}
