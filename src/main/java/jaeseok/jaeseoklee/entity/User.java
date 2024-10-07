package jaeseok.jaeseoklee.entity;

import jaeseok.jaeseoklee.dto.user.UpdateDto;
import jaeseok.jaeseoklee.entity.schedule.Schedule;
import jaeseok.jaeseoklee.entity.student.Student;
import jaeseok.jaeseoklee.entity.websocket.Chat;
import jaeseok.jaeseoklee.entity.websocket.ChatRoom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "user")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    @Column(unique = true, nullable = false, length = 50, name = "user_id")
    private String userId;
    @Column(nullable = false, name = "user_pw")
    private String userPw;
    @Column(nullable = false, name = "user_name")
    private String userRealName;
    @Column(unique = true, nullable = false, name = "user_num")
    private String userNum;
    @Column(nullable = false, name = "user_date")
    private String userDate;
    @CreationTimestamp
    @Column(nullable = false, name = "user_join")
    private LocalDateTime userJoin;
    @Column(unique = true, nullable = false, length = 50, name = "user_email")
    private String userEmail;
    @Column(nullable = false, name = "school_name")
    private String schoolName;
    @Column(nullable = false, name = "class_num")
    private int classNum;


    @ElementCollection(fetch = FetchType.EAGER) // "USER" 역할을 검증해야하기 때문에 즉시로딩 사용
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "uid"))
    @Column(name = "role")
    private List<String> roles;

    //    getAuthorities() 사용자 권한을 반환하는 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return userPw;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> student;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chat;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedule;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRoomCreator;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRoomParticipant;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<SeatTable> seatTable;

    public void update(UpdateDto updateDto, String creationUserNumDash) {
        this.userRealName = updateDto.getUserName();
        this.userNum = creationUserNumDash;
        this.schoolName = updateDto.getSchoolName();
        this.classNum = updateDto.getClassNum();
    }

    public void updatePassword(String hashPassword){
        this.userPw = hashPassword;
    }

    public User(String userEmail) {
        this.userEmail = userEmail;
    }

}
