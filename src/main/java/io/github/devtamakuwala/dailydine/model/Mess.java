package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.firebase.database.annotations.NotNull;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Objects;

/**
 * Model for Mess
 * */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tblMess")
@Data
@Getter
@Setter
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
}
