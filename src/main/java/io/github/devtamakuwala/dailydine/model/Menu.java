package io.github.devtamakuwala.dailydine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.devtamakuwala.dailydine.enums.MealType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mess_id", referencedColumnName = "messId")
    @JsonBackReference("mess-menu")
    private Mess mess;

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
    @ColumnDefault("false")
    private boolean expired;
}
