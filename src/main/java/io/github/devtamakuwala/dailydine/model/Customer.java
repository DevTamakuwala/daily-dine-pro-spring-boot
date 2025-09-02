package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.devtamakuwala.dailydine.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Represents a Customer in the application, containing customer-specific details.
 * This entity extends AuditableEntity to automatically gain the createdBy, createdAt,
 * modifiedBy, and modifiedAt fields. This ensures that any changes to a customer's specific data
 * (like their subscription status) are tracked automatically, providing a complete audit trail.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tblCustomer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long customerId;
    /**
     * The user associated with this customer.
     * - @OneToOne: Defines a one-to-one relationship between Customer and User.
     * - fetch = FetchType.LAZY: Optimizes performance by only loading the associated User object from the database when it is explicitly accessed.
     * - @JoinColumn: Specifies the foreign key column in the `tblCustomer` table.
     * - @JsonBackReference: Prevents infinite recursion during JSON serialization by marking this as the "back" part of the relationship.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @JsonBackReference
    private User userId;
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfBirth;
//    private boolean visible;
}
