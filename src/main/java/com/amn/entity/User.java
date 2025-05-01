package com.amn.entity;

import com.amn.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // optional, good for abstract class inheritance
public abstract class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(unique = true, nullable = false)
    protected String email;

    @Column(nullable = false)
    protected String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Role role;

    @Column(nullable = false)
    protected String fullName;

    @Column(nullable = false)
    protected String phone;

    protected String address;

    // ==== UserDetails Implementation ====

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role); // role must implement GrantedAuthority
    }

    @Override
    public String getUsername() {
        return email;
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
    public User(String name, String email, String password, Role role) {
        this.fullName = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
