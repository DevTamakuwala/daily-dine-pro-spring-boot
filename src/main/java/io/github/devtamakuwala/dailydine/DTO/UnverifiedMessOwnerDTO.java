package io.github.devtamakuwala.dailydine.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.devtamakuwala.dailydine.model.Mess;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


/**
 * DTO for unverified Mess owners
 * */
/**
 * A Data Transfer Object (DTO) for transferring information about unverified mess owners.
 * This DTO is used to send a simplified view of the user to the client, without exposing the full User entity.
 * It provides a clear and concise representation of the data needed for the client to display unverified mess owners.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnverifiedMessOwnerDTO {
    // The unique identifier for the user.
    private int userId;
    // The user's email address.
    private String email;
    // The user's first name.
    private String firstName;
    // The user's last name.
    private String lastName;
    // The mess associated with the user.
    private Mess mess;
    // The date and time when the user was registered.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
    private Instant createdAt;

}
