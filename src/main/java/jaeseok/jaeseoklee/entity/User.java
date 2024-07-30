package jaeseok.jaeseoklee.entity;

import jaeseok.jaeseoklee.dto.LoginDto;
import jaeseok.jaeseoklee.dto.SignUpDto;
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
import java.util.Date;
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
    private String user_id;
    @Column(nullable = false)
    private String user_pw;
    @Column(nullable = false)
    private String user_name;
    @Column(unique = true, nullable = false)
    private String user_num;
    @Column(nullable = false)
    private String user_date;
    @Column(unique = true, nullable = false, length = 50)
    private String user_nickname;
    @Column(nullable = false)
    private LocalDateTime user_join;
    @Column(unique = true, nullable = false, length = 50)
    private String user_email;
    @Column(nullable = false)
    private int school_num;
    @Column(nullable = false)
    private int class_num;


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
        return user_pw;
    }

    @Override
    public String getUsername() {
        return user_id;
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
}
