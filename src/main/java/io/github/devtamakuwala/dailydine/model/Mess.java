package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.database.annotations.NotNull;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Model for Mess
 *
 */
@Entity
@Table(name = "tblMess")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
     * The name "user-mess" must match the name in the @JsonManagedReference in the User entity.
     * <p>
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
    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "mess", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Menu> menu;
//    private boolean visible;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mess mess = (Mess) o;
        return messId == mess.messId && Objects.equals(messName, mess.messName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messId, messName);
    }

    @Override
    public String toString() {
        return "Mess{" +
                "messId=" + messId +
                ", messName='" + messName + '\'' +
                '}';
    }
}
