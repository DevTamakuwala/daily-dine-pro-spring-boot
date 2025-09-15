package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.*;
import io.github.devtamakuwala.dailydine.enums.MealType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * Model for menu of mess
 *
 */
@Entity
@Table(name = "tblMenu")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mess_id", referencedColumnName = "messId")
    @JsonIgnore
    private Mess mess;

    @JsonProperty("mess")
    public Integer getMessId() {
        if (mess != null) {
            return mess.getMessId();
        }
        return null;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date date;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private String availableFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private String availableTill;
    private int price;
    private boolean expired;
}
