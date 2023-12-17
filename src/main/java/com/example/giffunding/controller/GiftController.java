package com.example.giffunding.controller;

import com.example.giffunding.dto.request.CreateGiftRequestDto;
import com.example.giffunding.dto.response.GiftResponseDto;
import com.example.giffunding.entity.Gift;
import com.example.giffunding.entity.User;
import com.example.giffunding.repository.UserRepository;
import com.example.giffunding.service.GiftService;
import com.example.giffunding.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gift")
@RequiredArgsConstructor
public class GiftController {
    private final GiftService giftService;
    private final UserRepository userRepository;
    private final StorageService storageService;

    //선물 생성
    //사진 처리 해야함
    @PostMapping("")
    public ResponseEntity<Long> createGift(@RequestBody CreateGiftRequestDto createGiftRequestDto) throws IOException {
        MultipartFile file = createGiftRequestDto.getPhoto();
        String photoUrl = storageService.uploadFile(file.getBytes(), file.getOriginalFilename());

        Optional<User> existedUser =  userRepository.findById(createGiftRequestDto.getUserId());
        if (existedUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Gift gift = Gift.builder()
                .user(existedUser.get())
                .name(createGiftRequestDto.getName())
                .totalPrice(createGiftRequestDto.getPrice())
                .photoUrl(photoUrl)
                .build();

        return ResponseEntity.ok(giftService.saveGift(gift));
    }

    //특정 유저 선물 리스트 가져오기
    @GetMapping("")
    public ResponseEntity<List<GiftResponseDto>> getGiftsByUserId(@RequestParam("user_id") Long userId) {
        List<Gift> gifts = giftService.getGiftsByUserId(userId);
        if (gifts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<GiftResponseDto> giftDtos = gifts.stream()
                .map(gift -> new GiftResponseDto(
                        gift.getId(),
                        gift.getName(),
                        gift.getTotalPrice(),
                        gift.getPrice(),
                        gift.getPhotoUrl()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(giftDtos);
    }

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
