package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.devtamakuwala.dailydine.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

/**
 * Model for Customer
 */
@Entity
@Table(name = "tblCustomer")
@Getter
@Setter
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
     * - @JsonBackReference("user-customer"): This is the "back" part of the reference, which prevents a serialization loop.
     * The name "user-customer" must match the name in the @JsonManagedReference in the User entity.
     * <p>
     * REFACTORING NOTE:
     * The field was renamed from 'userId' to 'user' to more accurately reflect that it holds a User object,
     * not just the ID. This improves code clarity and consistency.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @JsonBackReference("user-customer")
    private User user;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfBirth;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId == customer.customerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", status=" + status +
                '}';
    }
}
