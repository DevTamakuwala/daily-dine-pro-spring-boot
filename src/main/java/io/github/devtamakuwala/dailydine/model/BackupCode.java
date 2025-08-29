package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single-use backup code for a user.
 * This entity is used to store hashed backup codes that can be used to log in if the user loses access to their authenticator app.
 */
@Entity
@Table(name = "tblBackupCode")
@Data
@NoArgsConstructor
public class BackupCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code; // This will store the HASHED backup code

    /**
     * The user associated with this backup code.
     * - @ManyToOne: Defines a many-to-one relationship between BackupCode and User.
     * - fetch = FetchType.LAZY: Ensures that the user is only loaded from the database when it is explicitly accessed.
     * - @JoinColumn: Specifies the foreign key column in the `tblBackupCode` table.
     * - @JsonIgnore: Prevents the user object from being serialized and sent back in API responses, which avoids circular dependencies and unnecessary data exposure.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Prevents sending user data back with the code
    private User user;

    public BackupCode(String code, User user) {
        this.code = code;
        this.user = user;
    }
}