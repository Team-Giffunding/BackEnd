package com.example.giffunding.dto.request;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateGiftRequestDto {
    private Long userId;
    private String name;
    private Integer price;
    private String photoUrl;
}
