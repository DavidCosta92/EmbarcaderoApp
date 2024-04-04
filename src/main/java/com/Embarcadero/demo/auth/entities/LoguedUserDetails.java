package com.Embarcadero.demo.auth.entities;

import com.Embarcadero.demo.model.entities.ImageFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class LoguedUserDetails {
    String token;
    Integer id;
    String username;
    String lastName;
    String firstName;
    String dni;
    String phone;
    String emergency_phone;
    String email;
    Role role;
    // List<ImageFile> imageFiles;
    ImageFile userProfileImage;
    Collection<? extends GrantedAuthority> authorities;

    /*
    public ImageFile getUserProfileImage(){
        List<ImageFile> profileImageList = getImageFiles().stream().filter(img -> img.getUsedFor().equals("profile")).collect(Collectors.toList());
        if(profileImageList.size()>0){
            return profileImageList.get(0);
        }
        return new ImageFile();
    }
    */
}
