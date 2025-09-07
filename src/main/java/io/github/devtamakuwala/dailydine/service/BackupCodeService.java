package io.github.devtamakuwala.dailydine.service;

import io.github.devtamakuwala.dailydine.repository.BackupCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service to handle backup codes from database
 * */
@Service
public class BackupCodeService {

    @Autowired
    private BackupCodeRepository backupCodeRepository;

    public void removeBackupCodeByUserId(int userId) {
        backupCodeRepository.deleteByUserId(userId);
    }
}
