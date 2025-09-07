package io.github.devtamakuwala.dailydine.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO for login with Backup code
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackupCodeLoginDTO {
    private String email;
    private String password;
    private String backupCode;
}