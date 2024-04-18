package com.Embarcadero.demo.auth;

import com.Embarcadero.demo.auth.entities.*;
import com.Embarcadero.demo.auth.jwt.JwtService;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDtoArray;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDtoArray;
import com.Embarcadero.demo.model.dtos.user.UserReadDto;
import com.Embarcadero.demo.model.dtos.user.UserReadDtoArray;
import com.Embarcadero.demo.model.dtos.user.UserUpdateDto;
import com.Embarcadero.demo.model.entities.Shift;
import com.Embarcadero.demo.model.mappers.UserMapper;
import com.Embarcadero.demo.utils.MailManager;
import com.Embarcadero.demo.utils.Validator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MailManager mailManager;
    @Autowired
    private Validator validator;

    @Autowired
    private UserMapper userMapper;

    public UserReadDto editUserRoleById(Integer idUser, Role newRole){
        Optional<User> userOp = userRepository.findById(idUser);
        if (userOp.isPresent()) {
            User user = userOp.get();
            user.setRole(newRole);
            userRepository.save(user);
            return userMapper.toReadDto(user);
        } else {
            throw new NotFoundException("No se encontro usuario por ID: "+idUser);
        }
    }
    public UserReadDtoArray getAllUsers (String dni, String fullName, Integer page, Integer size, String sortBy){
        Page<User> results;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        if (dni != null) {
            results = userRepository.findAllByDniContains(dni, pageable);
        } else if (dni == null && !fullName.equals("")){
            results = userRepository.getAllByFullName(fullName, pageable);
        } else {
            results = userRepository.findAll(pageable);
        }
        Page pagedResults = results.map(entity -> userMapper.toReadDto(entity));

        return UserReadDtoArray.builder()
                .users(pagedResults.getContent())
                .total_results(pagedResults.getTotalElements())
                .results_per_page(size)
                .current_page(page)
                .pages(pagedResults.getTotalPages())
                .sort_by(sortBy)
                .build();
    }


    public UserReadDtoArray getAllLifeguards(String dni, String fullName, Integer page, Integer size, String sortBy){
        Page<User> results;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        if (dni != null) {
            results = userRepository.findAllByRoleAndDniContains(Role.LIFEGUARD,dni, pageable);
        } else if (dni == null && !fullName.equals("")){
            results = userRepository.getAllByRoleAndFullName(Role.LIFEGUARD,fullName, pageable);
        } else {
            results = userRepository.findAllByRole(Role.LIFEGUARD, pageable);
        }
        Page pagedResults = results.map(entity -> userMapper.toReadDto(entity));

        return UserReadDtoArray.builder()
                .users(pagedResults.getContent())
                .total_results(pagedResults.getTotalElements())
                .results_per_page(size)
                .current_page(page)
                .pages(pagedResults.getTotalPages())
                .sort_by(sortBy)
                .build();
    }


    public AuthResponse register(RegisterRequest registerRequest) {
        if (! registerRequest.getPassword1().equals(registerRequest.getPassword2())) {
            throw new com.Embarcadero.demo.exceptions.customsExceptions.InvalidValueException("Passwords no coinciden!");
        }
        validateNewUsername(registerRequest.getUsername());
        validateNewDni(registerRequest.getDni());
        validateNewEmail(registerRequest.getEmail());

        User user = new User().builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword1()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .phone(registerRequest.getPhone())
                .emergency_phone(registerRequest.getEmergency_phone())
                .dni(registerRequest.getDni())
                .email(registerRequest.getEmail())
                .role(createRoleByEmail(registerRequest.getEmail()))
                .build();


        userRepository.save(user);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registerRequest.getUsername() , registerRequest.getPassword1()));

        String sendStatus = mailManager.sendEmail(user.getEmail(), "Test servidor backend java", "Hola, GRACIAS POR REGISTRARTE "+user.getUsername()+"!");
        // log.info("NUEVO USUARIO => "+user.getUsername());

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .emailStatus(sendStatus)
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername() , loginRequest.getPassword()));
        UserDetails userDetails = userRepository
                .findByUsername(loginRequest.getUsername())
                .orElseThrow(()->new com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException(("User not found")));
        String token = jwtService.getToken(userDetails);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public LoguedUserDetails getLoguedUserDetails(HttpHeaders headers) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        User loguedUser = (User) securityContext.getAuthentication().getPrincipal();
        String token = jwtService.getTokenFromHeader(headers);

        return LoguedUserDetails
                .builder()
                .token(token)
                .id(loguedUser.getId())
                .username(loguedUser.getUsername())
                .firstName(loguedUser.getFirstName())
                .lastName(loguedUser.getLastName())
                .phone(loguedUser.getPhone())
                .emergency_phone(loguedUser.getEmergency_phone())
                .dni(loguedUser.getDni())
                .email((loguedUser.getEmail()))
                .role(loguedUser.getRole())
                // .imageFiles(loguedUser.getImageFiles())
                .userProfileImage(loguedUser.getUserProfileImage())
                .authorities(loguedUser.getAuthorities())
                .build();
    }

    public LoguedUserDetails editUserDetails (HttpHeaders headers, UserUpdateDto userUpdateDto){
        User loguedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // username, email y dni no se pueden editar. Rol solo puede ser editado por un admin mediante editUserDetailsById(String idUser, Role newRole) // todo rol puede ser editado por si mismo, debera tener un metodo especial que solo los admin puedan acceder y cambien solamente el rol => editUserDetailsById(String idUser, Role newRole)

        if(userUpdateDto.getFirstName() != null){
            validator.stringMinSize("Nombre", 2, userUpdateDto.getFirstName());
            validator.stringOnlyLetters("Nombre", userUpdateDto.getFirstName());
            loguedUser.setFirstName(userUpdateDto.getFirstName());
        }
        if(userUpdateDto.getLastName() != null){
            validator.stringMinSize("Apellido", 2, userUpdateDto.getLastName());
            validator.stringOnlyLetters("Apellido", userUpdateDto.getLastName());
            loguedUser.setLastName(userUpdateDto.getLastName());
        }
        if(userUpdateDto.getPhone() != null){
            validator.validPhoneNumber(userUpdateDto.getPhone());
            loguedUser.setPhone(userUpdateDto.getPhone());
        }
        if(userUpdateDto.getEmergency_phone() != null){
            validator.validPhoneNumber(userUpdateDto.getEmergency_phone());
            loguedUser.setEmergency_phone(userUpdateDto.getEmergency_phone());
        }
        // guardar loguedUser
        userRepository.save(loguedUser);
        return getLoguedUserDetails(headers);
    }

    public Boolean restorePassword(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) throw new com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException("Email no registrado");
        String tokenToRestore = jwtService.createTokenForRestorePassword(user.get().getUsername());
        mailManager.sendEmailToRestorePassword(email , tokenToRestore);
        log.warn(">> token enviado para restaurar cuenta: "+email+", token: " +tokenToRestore+" <<");
        return true;
    }


    public AuthResponse setNewPassword(RestorePassRequest restorePassRequest){
        if(!restorePassRequest.getPassword1().equals(restorePassRequest.getPassword2())) throw new com.Embarcadero.demo.exceptions.customsExceptions.InvalidValueException("Passwords no coinciden");
        if (jwtService.isTokenExpired(restorePassRequest.getToken())) throw new com.Embarcadero.demo.exceptions.customsExceptions.InvalidJwtException("Token expirado, vuelve a solicitar envio del token");

        String username = jwtService.getUsernameFromToken(restorePassRequest.getToken());
        User user = userRepository.findByUsername(username).get();

        user.setPassword(passwordEncoder.encode(validator.stringMinSize("Password",5, restorePassRequest.getPassword1())));
        userRepository.save(user);
        return login(new LoginRequest(username,restorePassRequest.getPassword1()));
    }

    public void validateNewUsername(String username){
        if(userRepository.existsByUsername(validator.stringOnlyLettersAndNumbers("Username", username))) throw new com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException("Username ya en uso!");
    }
    public void validateNewEmail(String email){
        // TODO VALIDAR TIPOS DE DATOS INPUTS email
        if(userRepository.existsByEmail(email)) throw new com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException("Email ya en uso!");
    }
    public boolean existByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public void validateNewDni(String dni){
        validator.stringMinSize("Dni",8, dni);
        if(userRepository.existsByDni(validator.stringOnlyNumbers("Dni" , dni))) throw new com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException("Dni ya en uso!");
    }

    public Role createRoleByEmail ( String email){
        // TODO EL ROL USER DEBE SER EL INICIAL, Y SOLO EL SUPER DEBERIA ASIGNAR LOS ROLES SEGUN CONDICIONES DE NEGOCIO??
        Role role = Role.USER;
        if (email.contains("@office")) role = Role.OFFICE;
        if (email.contains("@gv")) role = Role.LIFEGUARD;
        if (email.contains("super@")) role = Role.SUPER_ADMIN;
        if (email.contains("admin@") || email.contains("davidcst2991@")) role = Role.ADMIN;
        return role;
    }

}
