package com.example.giffunding.dto.request;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class CreateGiftRequestDto {
    private Long userId;
    private String name;
    private Integer price;
    private MultipartFile photo;
}
