package com.Embarcadero.demo.model.mappers;

import com.Embarcadero.demo.model.dtos.owner.OwnerAddDto;
import com.Embarcadero.demo.model.dtos.owner.OwnerReadDto;
import com.Embarcadero.demo.model.entities.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {

    public Owner addDtoToEntity (OwnerAddDto ownerAddDto){
        return Owner.builder()
                .dni(ownerAddDto.getDni())
                .phone(ownerAddDto.getPhone())
                .emergency_phone(ownerAddDto.getEmergency_phone())
                .address(ownerAddDto.getAddress())
                .notes(ownerAddDto.getNotes())
                .build();
    }
    public OwnerReadDto entityToReadDto (Owner owner){
        return OwnerReadDto.builder()
                .id(owner.getId())
                .dni(owner.getDni())
                .phone(owner.getPhone())
                .emergency_phone(owner.getEmergency_phone())
                .address(owner.getAddress())
                .notes(owner.getNotes())
                .build();
    }
    public OwnerAddDto entityToAddDto (Owner owner){
        return OwnerAddDto.builder()
                .dni(owner.getDni())
                .phone(owner.getPhone())
                .emergency_phone(owner.getEmergency_phone())
                .address(owner.getAddress())
                .notes(owner.getNotes())
                .build();
    }

}
