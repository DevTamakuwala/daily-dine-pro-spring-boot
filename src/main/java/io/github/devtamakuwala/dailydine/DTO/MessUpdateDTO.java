package io.github.devtamakuwala.dailydine.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessUpdateDTO {
    private String fname;
    private String lname;
    private long phoneNo;
    private String messName;
    private String address;
    private String city;
    private String state;
    private String zipCode;

}
