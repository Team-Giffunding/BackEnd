package com.example.giffunding.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class ReviewResponseDto {
    private Long userId;
    private String text;
}
