package io.github.devtamakuwala.dailydine.service;

import io.github.devtamakuwala.dailydine.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AdminDashboardService {

    private final UserRepository userRepository;

    public AdminDashboardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gets the total count of mess owners pending verification.
     * The result is cached in the "users" cache with the key "'pendingMessCount'".
     */
    @Cacheable(value = "users", key = "'pendingMessCount'")
    public Integer totalPendingMessVerification() {
        return userRepository.countPendingMessVerification();
    }

    @Cacheable(value = "users", key = "'totalMessCount'")
    public Integer totalMess() {
        return userRepository.countMess();
    }

    @Cacheable(value = "users", key = "'totalCustomersCount'")
    public Integer totalCustomers() {
        return userRepository.countAllCustomers();
    }
}
