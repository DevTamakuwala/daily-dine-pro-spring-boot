package io.github.devtamakuwala.dailydine.model;

import io.github.devtamakuwala.dailydine.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User userId;
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
    private boolean visible;
}
