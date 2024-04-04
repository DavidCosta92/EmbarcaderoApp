package com.Embarcadero.demo.services;

import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.model.entities.ImageFile;
import com.Embarcadero.demo.model.repositories.ImageFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Autowired
    ImageFileRepository imageFileRepository;

    @Autowired
    UserService userService;


    public String uploadUserProfileImg(String idUser, MultipartFile file) throws IOException {
        String date = String.valueOf(new Date().toInstant().getEpochSecond());
        String fileName = file.getOriginalFilename()+date;

        User user = userService.findById(Integer.parseInt(idUser));

        ImageFile imageFile = ImageFile.builder()
                        .name(fileName)
                        .type(file.getContentType())
                        .data(file.getBytes())
                        .usedFor("profile")
                        .build();
        imageFileRepository.save(imageFile);

        List<ImageFile> userImages = user.getImageFiles();
        userImages.add(imageFile);

        // desasociar imagen de perfil, sin eliminarla
        ImageFile userProfileImage = user.getUserProfileImage();
        userProfileImage.setUsedFor("");
        imageFileRepository.save(userProfileImage);

        return "File upload "+fileName;
    }

    public ImageFile getUserProfileImg(String idUser){
        return userService.findById(Integer.parseInt(idUser)).getUserProfileImage();
    }

    // todo para archivo excel, debo guardar archivo en carpetas de proyecto y desde ahi analizarlo y demas..
    // todo ver este video => https://www.youtube.com/watch?v=h5YW-_xgmlo


}
