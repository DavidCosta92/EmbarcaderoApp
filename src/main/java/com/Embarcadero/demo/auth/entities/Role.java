package com.Embarcadero.demo.auth.entities;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    USER(
            Set.of(
                    Permission.READ_ALL
            )
    ),
    ADMIN(
            Set.of(
                    Permission.READ_ALL,
                    Permission.EDIT_ALL
            )
    ),
    SUPER_ADMIN(
            Set.of(
            Permission.READ_ALL,
            Permission.EDIT_ALL,
            Permission.DELETE_ALL
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities(){
        List<SimpleGrantedAuthority> auths = getPermissions()
                .stream()
                .map(permision -> new SimpleGrantedAuthority(permision.getPermission()))
                .collect(Collectors.toList());
        auths.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return  auths;
    }
}
