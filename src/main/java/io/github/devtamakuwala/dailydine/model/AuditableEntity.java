package io.github.devtamakuwala.dailydine.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * This is an abstract base class for entities that require auditing fields.
 * By having other entities (like User, Customer, Mess) extend this class,
 * we avoid code duplication and ensure a consistent approach to auditing across the application.
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity {

    /**
     * This field will store the ID of the user who created the entity.
     * The @CreatedBy annotation tells Spring Data JPA to populate this field
     * with the value returned by our AuditorAware implementation when the entity is first saved.
     * The type was changed to Integer to match the User entity's ID type.
     */
    @CreatedBy
    @Column(name = "created_by")
    private Integer createdBy;

    /**
     * This field will store the exact timestamp when the entity was created.
     * The @CreatedDate annotation tells Spring Data JPA to automatically set this field
     * to the current time when the entity is first saved.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    /**
     * This field will store the ID of the user who last modified the entity.
     * The @LastModifiedBy annotation tells Spring Data JPA to populate this field
     * with the value from our AuditorAware implementation every time the entity is updated.
     * The type was changed to Integer to match the User entity's ID type.
     */
    @LastModifiedBy
    @Column(name = "modified_by")
    private Integer modifiedBy;

    /**
     * This field will store the exact timestamp of the last modification.
     * The @LastModifiedDate annotation tells Spring Data JPA to automatically update this field
     * to the current time every time the entity is updated.
     */
    @LastModifiedDate
    @Column(name = "modified_at")
    private Instant modifiedAt;
}
