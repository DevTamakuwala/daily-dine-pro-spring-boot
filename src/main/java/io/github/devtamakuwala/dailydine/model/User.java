package io.github.devtamakuwala.dailydine.model;

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
    @OneToOne(mappedBy = "ownerId")
    private Mess mess;
    @OneToOne(mappedBy = "userId")
    private Customer customer;
}


/*
* Commit message
*
*
*
* */