package com.example.giffunding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GiftResponseDto {
    private Long giftId;
    private String name;
    private Integer totalPrice;
    private Integer price;
    private String photoUrl;
}

