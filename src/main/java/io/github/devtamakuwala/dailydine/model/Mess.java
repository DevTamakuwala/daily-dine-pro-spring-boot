package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "tblMess")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mess {
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
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date createdDate;
    private String lastModifiedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date lastModifiedDate;
    private boolean visible;
}
