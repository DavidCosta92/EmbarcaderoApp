package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.model.dtos.user.UserReadDto;
import com.Embarcadero.demo.model.dtos.user.UserStaffReadDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserReadDto toReadDto(User u){
        return UserReadDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .dni(u.getDni())
                .phone(u.getPhone())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .role(u.getRole())
                .email(u.getEmail())
                .build();
    }


    public UserStaffReadDto toStaffReadDto(User u){
        return UserStaffReadDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .dni(u.getDni())
                .phone(u.getPhone())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .role(u.getRole())
                .isAccountNonExpired(u.isAccountNonExpired())
                .isAccountNonLocked(u.isAccountNonLocked())
                .isCredentialsNonExpired(u.isCredentialsNonExpired())
                .isEnabled(u.isEnabled())
                .authorities(u.getAuthorities())
                .build();
    }
}
