package com.example.giffunding.controller;

import com.example.giffunding.dto.request.CreateFundRequestDto;
import com.example.giffunding.entity.Fund;
import com.example.giffunding.entity.User;
import com.example.giffunding.repository.UserRepository;
import com.example.giffunding.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/fund")
@RequiredArgsConstructor
public class FundController {
    private final FundService fundService;
    private final UserRepository userRepository;

    //펀딩 생성
    //선물 현재 가격 추가 로직 해야함
    @PostMapping("")
    public ResponseEntity<Long> createFund(@RequestBody CreateFundRequestDto createFundRequestDto) {
        Optional<User> existedUser =  userRepository.findById(createFundRequestDto.getUserId());
        if (existedUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Fund fund = Fund.builder()
                .user(existedUser.get())
                .nickname(createFundRequestDto.getNickname())
                .price(createFundRequestDto.getPrice())
                .text(createFundRequestDto.getText())
                .build();

        return ResponseEntity.ok(fundService.saveFund(fund));
    }
}
