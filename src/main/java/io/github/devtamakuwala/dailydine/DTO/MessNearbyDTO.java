package io.github.devtamakuwala.dailydine.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessNearbyDTO {
    private int messId;
    private String messName;
    private Double latitude;
    private Double longitude;
    private Double distanceMeters;

}
