package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.model.entities.ImageFile;
import com.Embarcadero.demo.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files/")
public class FilesController {

    @Autowired
    FileService fileService;

    @PostMapping("user/profileImg/{idUser}")
    ResponseEntity<String> uploadUserProfileImg (@PathVariable String idUser, @RequestParam("file") MultipartFile file) throws IOException {
        // TODO deberia responder algun dto? Pero por el momento, solo un mensaje de todo ok
        return new ResponseEntity<>(fileService.uploadUserProfileImg(idUser, file), HttpStatus.CREATED);
    }

    @GetMapping("user/profileImg/{idUser}")
    ResponseEntity<byte[]> getUserProfileImg ( @PathVariable String idUser){
        ImageFile img = fileService.getUserProfileImg(idUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, img.getType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + img.getName()+"\"")
                .body(img.getData());
    }

    // todo endpoint para subir archivo excel

}
