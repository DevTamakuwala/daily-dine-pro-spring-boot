package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.devtamakuwala.dailydine.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a User in the application.
 */
@Entity
@Table(name = "tblUser")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private long phoneNo;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean active;
    /**
     * The mess associated with this user.
     * - fetch = FetchType.LAZY: Optimizes performance by only loading the associated Mess object from the database when it is explicitly accessed.
     * - @JsonManagedReference("user-mess"): This is the "front" part of the reference, which prevents a serialization loop.
     *   The name "user-mess" must match the name in the @JsonBackReference in the Mess entity.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("user-mess")
    private Mess mess;
    /**
     * The customer associated with this user.
     * - fetch = FetchType.LAZY: Optimizes performance by only loading the associated Customer object from the database when it is explicitly accessed.
     * - @JsonManagedReference("user-customer"): This is the "front" part of the reference, which prevents a serialization loop.
     *   The name "user-customer" must match the name in the @JsonBackReference in the Customer entity.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("user-customer")
    private Customer customer;
    @Column(nullable = false)
    @ColumnDefault("0")
    private boolean mfaEnabled = false;
    private String mfaSecret;

    /**
     * A list of backup codes for the user.
     * - fetch = FetchType.LAZY: Optimizes performance by only loading the backup codes from the database when they are explicitly accessed.
     * - @JsonManagedReference("user-backup-codes"): This is the "front" part of the reference, which prevents a serialization loop.
     *   The name "user-backup-codes" must match the name in the @JsonBackReference in the BackupCode entity.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("user-backup-codes")
    private List<BackupCode> backupCodes = new ArrayList<>();

    public User(int userId, String email, String password, String firstName, String lastName, long phoneNo, Role role, boolean active, Customer customer, boolean mfaEnabled, String mfaSecret, List<BackupCode> backupCodes) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.role = role;
        this.active = active;
        this.customer = customer;
        this.mfaEnabled = mfaEnabled;
        this.mfaSecret = mfaSecret;
        this.backupCodes = backupCodes;
    }

    public User(int userId, String email, String password, String firstName, String lastName, long phoneNo, Role role, boolean active, Mess mess, boolean mfaEnabled, String mfaSecret, List<BackupCode> backupCodes) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.role = role;
        this.active = active;
        this.mess = mess;
        this.mfaEnabled = mfaEnabled;
        this.mfaSecret = mfaSecret;
        this.backupCodes = backupCodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", role=" + role +
                ", active=" + active +
                '}';
    }
}
