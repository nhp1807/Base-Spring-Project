package com.example.demo.cache;

import com.example.demo.repository.UserRepository;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class BloomFilterUserId {

    private final BloomFilter<Long> bloomFilter;

    public BloomFilterUserId(UserRepository userRepository) {
        this.bloomFilter = BloomFilter.create(Funnels.longFunnel(), 10000, 0.01);
        List<Long> userIds = userRepository.getUserIds();
        for (Long userId : userIds) {
            bloomFilter.put(userId);
        }

        log.info("BloomFilterUserId initialized with {} userIds", userIds.size());
    }

    public boolean contains(Long userId) {
        return bloomFilter.mightContain(userId);
    }

    public void add(Long userId) {
        bloomFilter.put(userId);
    }
}
