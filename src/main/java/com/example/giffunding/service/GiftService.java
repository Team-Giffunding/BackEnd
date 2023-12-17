package com.example.giffunding.service;

import com.example.giffunding.entity.Gift;
import com.example.giffunding.repository.GiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GiftService {
    private final GiftRepository giftRepository;
    @Transactional
    public Long saveGift(Gift gift) {
        giftRepository.save(gift);
        return gift.getId();
    }

    public List<Gift> getGiftsByUserId(Long userId) {
        return giftRepository.findByUserId(userId);
    }

    @Transactional
    public void updateGiftPrice(Gift gift, Integer price) {
        gift.updateGiftPrice(price);
    }

    public Optional<Gift> getGift(Long gift_id){
        return giftRepository.findById(gift_id);
    }
    @Transactional
    public Long deleteGift(Long id) {
        giftRepository.deleteById(id);
        return id;
    }
}
