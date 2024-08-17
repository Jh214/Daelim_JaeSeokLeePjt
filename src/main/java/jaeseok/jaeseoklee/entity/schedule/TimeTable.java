package jaeseok.jaeseoklee.entity.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jaeseok.jaeseoklee.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "Timetable")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TimeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tId;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek; // 시간 ex) 월,화,수,목,금
    @Column(name = "period", nullable = false)
    private int period; // 장소

    @ManyToOne(fetch = LAZY)
    @JsonIgnore
    private Subjects subjects;

    @ManyToOne(fetch = LAZY)
    @JsonIgnore
    private User user;

}
