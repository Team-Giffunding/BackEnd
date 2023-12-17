package com.example.giffunding.controller;

import com.example.giffunding.config.JwtTokenProvider;
import com.example.giffunding.dto.request.CreateUserRequestDto;
import com.example.giffunding.dto.request.LoginRequestDto;
import com.example.giffunding.dto.response.KakaoLoginResponseDto;
import com.example.giffunding.dto.response.ResponseDto;
import com.example.giffunding.dto.response.TokenResponseDto;
import com.example.giffunding.entity.User;
import com.example.giffunding.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("")
    public ResponseEntity<Long> createUser(@RequestBody CreateUserRequestDto requestDto) {
        User newUser = userService.createUser(requestDto);
        return ResponseEntity.ok(newUser.getId());
    }

    //카카오 로그인
    @PostMapping(value = "/kakao-login")
    public KakaoLoginResponseDto kakaoLogin(@RequestBody LoginRequestDto loginRequestDto)  {

        KakaoLoginResponseDto kakaoLoginResponseDto = userService.kakaoLogin(loginRequestDto.getCode());
        LOGGER.info("카카오 로그인 완료");

        return kakaoLoginResponseDto;
    }

    //로그아웃
    @DeleteMapping("/logout")
    public ResponseDto logout(HttpServletRequest request)  {
        String userId = jwtTokenProvider.getUserId(request);
        ResponseDto logoutResponseDto = userService.logout(Long.valueOf(userId));
        LOGGER.info("로그아웃 완료");

        return logoutResponseDto;
    }

    //액세스 토큰 재발급
    @GetMapping(value = "/reissue-token")
    public TokenResponseDto reissueToken(HttpServletRequest request)  {

        String userId = jwtTokenProvider.getUserId(request);
        String refreshToken = jwtTokenProvider.resolveToken(request);
        TokenResponseDto reissueTokenResponseDto = userService.reissueToken(refreshToken, Long.valueOf(userId));
        LOGGER.info("토큰 재발급 완료");

        return reissueTokenResponseDto;
    }

}

