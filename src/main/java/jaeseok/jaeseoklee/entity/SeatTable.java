//package jaeseok.jaeseoklee.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jaeseok.jaeseoklee.entity.student.Student;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import static jakarta.persistence.FetchType.LAZY;
//
//@Table(name = "seat_table")
//@Entity
//@Data
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class SeatTable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long seatId;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "studentId")
//    @JsonIgnore // 반환 데이터로 student 데이터는 제외
//    private Student student;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "uid")
//    @JsonIgnore  // 반환 데이터로 user 데이터는 제외
//    private User user;
//}