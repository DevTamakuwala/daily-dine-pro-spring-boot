package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.devtamakuwala.dailydine.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a User in the application.
 * This entity now extends AuditableEntity to automatically inherit the createdBy, createdAt,
 * modifiedBy, and modifiedAt fields. This approach centralizes the auditing logic,
 * removes code duplication, and leverages Spring Data JPA's automatic auditing capabilities.
 * The old, manually managed audit fields have been removed in favor of this more robust system.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tblUser")
@Data
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
     * - @JsonManagedReference: Prevents infinite recursion during JSON serialization by marking this as the "front" part of the relationship.
     */
    @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Mess mess;
    /**
     * The customer associated with this user.
     * - fetch = FetchType.LAZY: Optimizes performance by only loading the associated Customer object from the database when it is explicitly accessed.
     * - @JsonManagedReference: Prevents infinite recursion during JSON serialization by marking this as the "front" part of the relationship.
     */
    @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Customer customer;
    @Column(nullable = false)
    @ColumnDefault("0")
    private boolean mfaEnabled = false;
    private String mfaSecret;

    /**
     * A list of backup codes for the user.
     * - fetch = FetchType.LAZY: Optimizes performance by only loading the backup codes from the database when they are explicitly accessed.
     * - @JsonManagedReference: Prevents infinite recursion during JSON serialization by marking this as the "front" part of the relationship.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
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
}