package com.Embarcadero.demo.services;

import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.model.dtos.owner.OwnerAddDto;
import com.Embarcadero.demo.model.dtos.owner.OwnerUpdateDto;
import com.Embarcadero.demo.model.entities.Owner;
import com.Embarcadero.demo.model.mappers.OwnerMapper;
import com.Embarcadero.demo.model.repositories.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {
    @Autowired
    private OwnerMapper ownerMapper;

    @Autowired
    private OwnerRepository ownerRepository;

    public Owner getOrAddOwner(OwnerAddDto ownerAddDto){
        validateNewOwner(ownerAddDto);
        if(ownerAddDto.getPhone() != null && ownerAddDto.getEmergency_phone() != null && ownerAddDto.getAddress() != null && ownerAddDto.getNotes() != null && ownerAddDto.getDni() != null ){
           ownerRepository.save(ownerMapper.addDtoToEntity(ownerAddDto));
        }
        return ownerRepository.findByDni(ownerAddDto.getDni());
    }
    public Owner updateOwner (Owner bdOwner , OwnerUpdateDto ownerUpdateDto){
        // SI OWNER UPDATE TRAE TODOS LOS DATOS.. CREO UN OWNER NUEVO Y LO devuelvo
            // ownerRepository.save(ownerMapper.addDtoToEntity(ownerUpdateDto));

        // SI NO TIENE TODOS LOS DATOS, entonces:
            // Uso el bdOwner y actualizo los datos que son distintos
                // reviso que datos vienen y los valido
                // si son validos, los agrego al owner en bd y mando a guardar
                // al final debo retornar el owner actualizado

        return new Owner();
    }
    public void validateOwnerNewMatricula(OwnerAddDto ownerAddDto){
        if(ownerAddDto.getPhone() == null && ownerAddDto.getEmergency_phone() == null && ownerAddDto.getAddress() == null && ownerAddDto.getNotes() == null && ownerAddDto.getDni() != null ){ // si solo viene dni, asumo que ya deberia existir la persona, por lo que deberia traer de bd
            validateAlreadyReportedOwner(ownerAddDto);
        } else {
            validateNewOwner(ownerAddDto);
        }
    }
    public void validateNewOwner(OwnerAddDto ownerAddDto){
        // TODO VALIDAR DATOS DE INGRESO
        // TODO si persona viene con todos los datos, asumo que quiere crear una persona nueva, por tanto valido que no este duplicado el dni y que los otros esten bien
        if(ownerRepository.existsByDni(ownerAddDto.getDni())) throw new AlreadyExistException("Persona ya existe con ese dni");
    }
    public void validateAlreadyReportedOwner(OwnerAddDto ownerAddDto){
        if(!ownerRepository.existsByDni(ownerAddDto.getDni())) throw new AlreadyExistException("Persona no existe, revisa dni o crea una nueva persona");
    }

}
