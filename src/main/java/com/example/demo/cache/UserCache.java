package com.example.demo.cache;

import com.example.demo.enums.ErrorCode;
import com.example.demo.exception.AppException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Component;

@Component
public class UserCache {
    private final LoadingCache<Long, User> userCache;

    public UserCache(UserRepository userRepository) {
        this.userCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(new com.google.common.cache.CacheLoader<Long, User>() {
                    @Override
                    public User load(Long userId) throws Exception {
                        return userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
                    }
                });
    }

    public User get(Long userId) {
        try {
            return userCache.get(userId);
        } catch (Exception e) {
            // Nếu là AppException thì throw lại để GlobalExceptionHandler xử lý
            if (e.getCause() instanceof AppException) {
                throw (AppException) e.getCause();
            }
            // Nếu là ExecutionException chứa AppException
            if (e instanceof java.util.concurrent.ExecutionException && e.getCause() instanceof AppException) {
                throw (AppException) e.getCause();
            }
            // Các exception khác thì throw lại
            throw new RuntimeException("Failed to get user from cache", e);
        }
    }

    public void put(Long userId, User user) {
        userCache.put(userId, user);
    }

    public void invalidate(Long userId) {
        userCache.invalidate(userId);
    }

    public void clear() {
        userCache.invalidateAll();
    }

    public long size() {
        return userCache.size();
    }

    public String getStats() {
        return userCache.stats().toString();
    }
}
