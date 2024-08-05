package jaeseok.jaeseoklee.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private String userName;
    @Column(unique = true, nullable = false, name = "user_num")
    private String userNum;
    @Column(nullable = false, name = "user_date")
    private String userDate;
    @Column(unique = true, nullable = false, length = 50, name = "user_nickname")
    private String userNickname;
    @Column(nullable = false, name = "user_join")
    private LocalDateTime userJoin;
    @Column(unique = true, nullable = false, length = 50, name = "user_email")
    private String userEmail;
    @Column(nullable = false, name = "school_num")
    private int schoolNum;
    @Column(nullable = false, name = "class_num")
    private int classNum;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roles;

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

    @OneToMany(mappedBy = "user", cascade =  CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnoreProperties("user")
    private List<Student> student;
}
