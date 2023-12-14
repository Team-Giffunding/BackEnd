package com.example.giffunding.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@Builder
public class CreateReviewRequestDto {
    private Long userId;
    private String text;

}
