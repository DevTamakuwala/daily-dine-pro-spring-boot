package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.devtamakuwala.dailydine.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    // CascadeType.ALL ensures that any operations (persist, merge, remove, etc.) on a User entity
    // are automatically cascaded to the associated Mess entity. This simplifies data management by
    // avoiding the need to explicitly save the Mess entity before saving the User.
    @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Mess mess;
    // Similarly, CascadeType.ALL is used for the Customer entity to ensure that when a User is saved,
    // the associated Customer is also saved automatically. This resolves the TransientObjectException
    // that occurred when trying to save a User with an unsaved Customer.
    @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Customer customer;
    private boolean mfaEnabled = false;
    private String mfaSecret;

    public User(int userId, String email, String password, String firstName, String lastName, long phoneNo, Role role, boolean active, Customer customer) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.role = role;
        this.active = active;
        this.customer = customer;
    }

    public User(int userId, String email, String password, String firstName, String lastName, long phoneNo, Role role, boolean active, Mess mess) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.role = role;
        this.active = active;
        this.mess = mess;
    }
}