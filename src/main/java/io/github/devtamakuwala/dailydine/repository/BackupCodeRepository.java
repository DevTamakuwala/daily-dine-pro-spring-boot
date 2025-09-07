package io.github.devtamakuwala.dailydine.repository;

import io.github.devtamakuwala.dailydine.model.BackupCode;
import io.github.devtamakuwala.dailydine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for backup codes
 * */
@Repository
public interface BackupCodeRepository extends JpaRepository<BackupCode, Long> {
    @Modifying
    @Query("DELETE FROM BackupCode bc WHERE bc.user.userId = :userId")
    void deleteByUserId(int userId);
}