package com.example.sns.repository;

import com.example.sns.dto.auth.InfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, InfoDTO> redisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(InfoDTO infoDTO) {
        String key = getKey(infoDTO.getUsername());
        log.info("Set User to Redis {} , {}", key, infoDTO);
        redisTemplate.opsForValue().set(key, infoDTO, USER_CACHE_TTL);
    }

    public Optional<InfoDTO> getUser(String username) {
        String key = getKey(username);
        InfoDTO infoDTO = redisTemplate.opsForValue().get(key);
        log.info("get data from Redis {} , {}", key, infoDTO);
        return Optional.ofNullable(infoDTO);
    }

    private String getKey(String username) {
        return "USER_DTO:" + username;
    }
}
