package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.firebase.database.annotations.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    @JsonBackReference
    private User userId;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date establisheDate;
    private String imageName;
    private String imageType;
    @Lob
    private byte[] imageData;
//    private boolean visible;
}
