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
    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @JsonBackReference
    private User userId;
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfBirth;
//    private boolean visible;
}
