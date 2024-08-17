package jaeseok.jaeseoklee.entity.schedule;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Location")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationId;
    @Column(name = "locationName", nullable = false)
    private String locationName;
}
