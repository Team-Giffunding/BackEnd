package com.example.giffunding.controller;

import com.example.giffunding.dto.request.CreateGiftRequestDto;
import com.example.giffunding.entity.Gift;
import com.example.giffunding.entity.User;
import com.example.giffunding.repository.UserRepository;
import com.example.giffunding.service.GiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/gift")
@RequiredArgsConstructor
public class GiftController {
    private final GiftService giftService;
    private final UserRepository userRepository;

    //선물 생성
    //사진 처리 해야함
    @PostMapping("")
    public ResponseEntity<Long> createGift(@RequestBody CreateGiftRequestDto createGiftRequestDto) {
        Optional<User> existedUser =  userRepository.findById(createGiftRequestDto.getUserId());
        if (existedUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Gift gift = Gift.builder()
                .user(existedUser.get())
                .name(createGiftRequestDto.getName())
                .totalPrice(createGiftRequestDto.getPrice())
                .photoUrl(createGiftRequestDto.getPhotoUrl())
                .build();

        return ResponseEntity.ok(giftService.saveGift(gift));
    }

    //특정 유저 선물 리스트 가져오기

    //특정 선물 삭제
    @DeleteMapping("")
    public ResponseEntity<Long> deleteGift(@RequestParam("gift_id") Long id) {
        Optional<Gift> savedGift = giftService.getGift(id);
        if (savedGift.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Long deletedGiftId = giftService.deleteGift(id);
        return ResponseEntity.ok(deletedGiftId);
    }
}
