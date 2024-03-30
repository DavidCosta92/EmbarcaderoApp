package com.Embarcadero.demo.auth.entities;

import com.Embarcadero.demo.model.entities.Shift;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    Integer id;

    @Column(nullable = false , unique = true)
    String username;

    @Column(nullable = false , unique = true)
    String dni;

    @Column(nullable = false , unique = true)
    String email;

    String phone;
    String password;

    String firstName;
    String lastName;

    // @ManyToMany(mappedBy = "staff", fetch = FetchType.EAGER)
    // private List<Shift> shifts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return role.getAuthorities();
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
