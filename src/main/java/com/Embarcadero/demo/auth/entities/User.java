package com.Embarcadero.demo.auth.entities;

import com.Embarcadero.demo.model.entities.ImageFile;
import com.Embarcadero.demo.model.entities.Record;
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
import java.util.stream.Collectors;

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
    String emergency_phone;
    String password;

    String firstName;
    String lastName;


    @OneToMany(fetch = FetchType.EAGER)
    List<ImageFile> imageFiles;

    // @ManyToMany(mappedBy = "staff", fetch = FetchType.EAGER)
    // private List<Shift> shifts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    Role role;

    public ImageFile getUserProfileImage(){
        List<ImageFile> profileImageList = getImageFiles().stream().filter(img -> img.getUsedFor().equals("profile")).collect(Collectors.toList());
        if(profileImageList.size()>0){
            return profileImageList.get(0);
        }
        return new ImageFile();
    }

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
