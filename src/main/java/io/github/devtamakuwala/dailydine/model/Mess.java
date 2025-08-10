package io.github.devtamakuwala.dailydine.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a Mess (food service provider) in the application.
 * This entity extends AuditableEntity to automatically inherit the createdBy, createdAt,
 * modifiedBy, and modifiedAt fields. This is crucial for tracking changes to a mess's profile,
 * such as updates to their address, menu, or contact information, all of which will now be
 * automatically audited by the system.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tblMess")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mess extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messId;
    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User ownerId;
    private String messName;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private long phone;
    private String email;
    private String imageName;
    private String imageType;
    @Lob
    private byte[] imageData;
    private boolean visible;
}
