package com.example.giffunding.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequestDto {
    private String userNumber;
    private String nickname;
    private String email;
}
