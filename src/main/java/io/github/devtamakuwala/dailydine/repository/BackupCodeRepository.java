package io.github.devtamakuwala.dailydine.repository;

import io.github.devtamakuwala.dailydine.model.BackupCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupCodeRepository extends JpaRepository<BackupCode, Long> {
}