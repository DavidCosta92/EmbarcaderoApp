package com.Embarcadero.demo.services;

import com.Embarcadero.demo.auth.UserRepository;
import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.exceptions.customsExceptions.InvalidValueException;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private Validator validator;

    @Autowired
    private UserRepository userRepository;

    public User getUserStaffMemberByDni(String staffMemberDni){
        validator.stringMinSize("Dni",8, staffMemberDni);
        Optional<User> staffMemberBd = userRepository.findByDni(staffMemberDni);
        if (staffMemberBd.isEmpty()) throw new NotFoundException("Staff user no encontrado, revisar dni o crear nuevo usuario");
        User userBd = staffMemberBd.get();
        if (!userBd.getRole().name().equals("LIFEGUARD") ) throw new InvalidValueException("Staff user no es guardavida, no puede ser agregado");
        if (!userBd.isEnabled() && !userBd.isAccountNonLocked() && !userBd.isAccountNonExpired() ) throw new InvalidValueException("Staff user no apto para ser agregado");
        return userBd;
    }
    public User findById(Integer id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new NotFoundException("No se encontro usuario con ID:"+id);
        return user.get();
    }
}
