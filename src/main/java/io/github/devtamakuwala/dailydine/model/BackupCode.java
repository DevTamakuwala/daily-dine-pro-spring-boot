package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * Represents a single-use backup code for a user.
 * This entity is used to store hashed backup codes that can be used to log in if the user loses access to their authenticator app.
 */
@Entity
@Table(name = "tblBackupCode")
@Getter
@Setter
@ToString(exclude = "user")
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
     * - @JsonBackReference("user-backup-codes"): This is the "back" part of the reference, which prevents a serialization loop.
     *   The name "user-backup-codes" must match the name in the @JsonManagedReference in the User entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-backup-codes")
    private User user;

    public BackupCode(String code, User user) {
        this.code = code;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackupCode that = (BackupCode) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
