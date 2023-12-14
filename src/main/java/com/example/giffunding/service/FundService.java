package com.example.giffunding.service;

import com.example.giffunding.entity.Fund;
import com.example.giffunding.repository.FundRepository;
import com.example.giffunding.repository.GiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FundService {
    private final FundRepository fundRepository;
    @Transactional
    public Long saveFund(Fund fund) {
        fundRepository.save(fund);
        return fund.getId();
    }
}
