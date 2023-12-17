package com.example.giffunding.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateFundRequestDto {
//    private Long userId;
    private Long giftId;
    private String nickname;
    private Integer price;
    private String text;
}
