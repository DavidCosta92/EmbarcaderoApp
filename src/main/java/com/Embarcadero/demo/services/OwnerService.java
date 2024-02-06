package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.model.dtos.owner.OwnerAddDto;
import com.Embarcadero.demo.model.dtos.owner.OwnerUpdateDto;
import com.Embarcadero.demo.model.entities.Owner;
import com.Embarcadero.demo.model.mappers.OwnerMapper;
import com.Embarcadero.demo.model.repositories.OwnerRepository;
import com.Embarcadero.demo.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {
    @Autowired
    private OwnerMapper ownerMapper;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private Validator validator;

    public Owner getOrAddOwner(OwnerAddDto ownerAddDto){
        validateOwnerNewMatricula(ownerAddDto);
        if(ownerAddDto.getPhone() != null && ownerAddDto.getEmergency_phone() != null && ownerAddDto.getAddress() != null && ownerAddDto.getNotes() != null && ownerAddDto.getDni() != null ){
           ownerRepository.save(ownerMapper.toEntity(ownerAddDto));
        }
        return ownerRepository.findByDni(ownerAddDto.getDni());
    }
    public Owner updateOwner (Owner bdOwner , OwnerUpdateDto newOwner){
        if(newOwner.getDni() != null && newOwner.getPhone() != null && newOwner.getEmergency_phone() != null && newOwner.getAddress() != null && newOwner.getNotes() != null ){
            return getOrAddOwner(ownerMapper.toAddDto(newOwner)); // SI OWNER UPDATE TRAE TODOS LOS DATOS.. CREO UN OWNER NUEVO Y LO devuelvo
        } else{
            // Uso el bdOwner y actualizo los datos que vienen, valido, guardo y devuelvo owner actualizado
            if (newOwner.getDni() != null) {
                bdOwner.setDni(newOwner.getDni());
            }
            if (newOwner.getPhone() != null) {
                validator.validPhoneNumber(newOwner.getPhone());
                bdOwner.setPhone(newOwner.getPhone());
            }
            if (newOwner.getEmergency_phone() != null) {
                validator.validPhoneNumber(newOwner.getEmergency_phone());
                bdOwner.setEmergency_phone(newOwner.getEmergency_phone());
            }
            if (newOwner.getAddress() != null) {
                validator.stringMinSize("Direccion", 5 , newOwner.getAddress());
                validator.stringOnlyLettersAndNumbers("Direccion", newOwner.getAddress());
                bdOwner.setAddress(newOwner.getAddress());
            }
            if (newOwner.getNotes() != null) {
                bdOwner.setNotes(newOwner.getNotes());
            }
            ownerRepository.save(bdOwner);
            return ownerRepository.findByDni(bdOwner.getDni());
        }
    }
    public void validateOwnerNewMatricula(OwnerAddDto ownerAddDto){
        if(ownerAddDto.getPhone() == null && ownerAddDto.getEmergency_phone() == null && ownerAddDto.getAddress() == null && ownerAddDto.getNotes() == null && ownerAddDto.getDni() != null ){ // si solo viene dni, asumo que ya deberia existir la persona, por lo que deberia traer de bd
            validateAlreadyReportedOwner(ownerAddDto);
        } else {
            validateNewOwner(ownerAddDto);
        }
    }
    public void validateNewOwner(OwnerAddDto ownerAddDto){
        validator.validPhoneNumber(ownerAddDto.getPhone());
        validator.validPhoneNumber(ownerAddDto.getEmergency_phone());
        validator.stringOnlyLettersAndNumbers("Direccion", ownerAddDto.getAddress());
        if(ownerRepository.existsByDni(ownerAddDto.getDni())) throw new AlreadyExistException("Persona ya existe con ese dni");
    }
    public void validateAlreadyReportedOwner(OwnerAddDto ownerAddDto){
        if(!ownerRepository.existsByDni(ownerAddDto.getDni())) throw new AlreadyExistException("Persona no existe, revisa dni o crea una nueva persona");
    }

}
