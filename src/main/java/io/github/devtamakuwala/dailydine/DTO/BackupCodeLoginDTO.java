package io.github.devtamakuwala.dailydine.DTO;

import lombok.Data;

@Data
public class BackupCodeLoginDTO {
    private String email;
    private String password;
    private String backupCode;
}