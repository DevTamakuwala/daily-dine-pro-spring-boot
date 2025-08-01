package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.devtamakuwala.dailydine.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "tblUser")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private long phoneNo;
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date createdDate;
    private String lastModifiedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date lastModifiedDate;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean active;
    @OneToOne(mappedBy = "ownerId")
    private Mess mess;
}
