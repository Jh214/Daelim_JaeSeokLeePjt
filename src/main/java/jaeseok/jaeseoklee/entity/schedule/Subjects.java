package jaeseok.jaeseoklee.entity.schedule;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Subjects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Subjects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int SubId;
    @Column(name = "SubName", nullable = false)
    private String SubName;

    @OneToMany(mappedBy = "subjects" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeTable> timeTable;
}
