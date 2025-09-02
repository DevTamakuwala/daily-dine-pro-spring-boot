package io.github.devtamakuwala.dailydine.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) for transferring information about unverified mess owners.
 * This DTO is used to send a simplified view of the user to the client, without exposing the full User entity.
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
    // The user's phone number.
    private long phoneNo;
}
