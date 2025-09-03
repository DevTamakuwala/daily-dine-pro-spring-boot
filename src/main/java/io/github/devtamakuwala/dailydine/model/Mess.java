package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.firebase.database.annotations.NotNull;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Objects;

/**
 * Represents a Mess (food service provider) in the application.
 * This entity extends AuditableEntity to automatically inherit the createdBy, createdAt,
 * modifiedBy, and modifiedAt fields.
 *
 * REFACTORING NOTE:
 * The @Data annotation was removed from this entity to prevent issues with bidirectional
 * relationships in JPA. @Data generates a problematic equals() and hashCode() implementation
 * that can cause infinite loops and persistence context corruption. It has been replaced
 * with @Getter, @Setter, and a safe @ToString implementation. The equals() and hashCode()
 * methods are now manually implemented based only on the primary key.
 */
@Entity
@Table(name = "tblMess")
@Getter
@Setter
@ToString(exclude = "user")
@AllArgsConstructor
@NoArgsConstructor
public class Mess extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messId;
    /**
     * The user associated with this mess.
     * - @OneToOne: Defines a one-to-one relationship between Mess and User.
     * - fetch = FetchType.LAZY: Optimizes performance by only loading the associated User object from the database when it is explicitly accessed.
     * - @JoinColumn: Specifies the foreign key column in the `tblMess` table.
     * - @JsonBackReference("user-mess"): This is the "back" part of the reference, which prevents a serialization loop.
     *   The name "user-mess" must match the name in the @JsonManagedReference in the User entity.
     *
     * REFACTORING NOTE:
     * The field was renamed from 'userId' to 'user' to more accurately reflect that it holds a User object,
     * not just the ID. This improves code clarity and consistency.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @JsonBackReference("user-mess")
    private User user;
    @NotNull
    private String messName;
    @NotNull
    private String address;
    private String city;
    private String state;
    @NotNull
    private String zipCode;
    @NotNull
    private long messPhoneNo;
    //    private String email;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date establisheDate;
    private String imageName;
    private String imageType;
    @Lob
    private byte[] imageData;
    private String latitude;
    private String longitude;
//    private boolean visible;

    /**
     * Overridden equals() method that compares entities based only on their primary key.
     * This is a best practice for JPA entities to ensure consistent behavior across
     * different persistence states (transient, managed, detached).
     * @param o The object to compare.
     * @return True if the objects are the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mess mess = (Mess) o;
        return messId == mess.messId;
    }

    /**
     * Overridden hashCode() method that generates a hash based only on the primary key.
     * This is a best practice for JPA entities to ensure consistent behavior when
     * managed in collections by the persistence context.
     * @return The hash code of the entity's ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(messId);
    }
}
