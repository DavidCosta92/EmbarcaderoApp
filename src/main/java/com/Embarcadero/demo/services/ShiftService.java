package com.Embarcadero.demo.services;

import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.exceptions.customsExceptions.AlreadyExistException;
import com.Embarcadero.demo.exceptions.customsExceptions.ForbiddenAction;
import com.Embarcadero.demo.exceptions.customsExceptions.InvalidValueException;
import com.Embarcadero.demo.exceptions.customsExceptions.NotFoundException;
import com.Embarcadero.demo.model.dtos.license.LicenseUpdateDto;
import com.Embarcadero.demo.model.dtos.records.RecordAddDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.dtos.records.RecordUpdateDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftAddDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDtoArray;
import com.Embarcadero.demo.model.dtos.shift.ShiftUpdateDto;
import com.Embarcadero.demo.model.dtos.staff.StaffMemberAddDto;
import com.Embarcadero.demo.model.dtos.user.UserDni;
import com.Embarcadero.demo.model.entities.Record;
import com.Embarcadero.demo.model.entities.Shift;
import com.Embarcadero.demo.model.entities.enums.Dam_enum;
import com.Embarcadero.demo.model.entities.enums.RecordState_enum;
import com.Embarcadero.demo.model.mappers.PersonMapper;
import com.Embarcadero.demo.model.mappers.ShiftMapper;
import com.Embarcadero.demo.model.repositories.ShiftRepository;
import com.Embarcadero.demo.utils.MailManager;
import com.Embarcadero.demo.utils.Validator;
import com.Embarcadero.demo.utils.reports.ShiftReportGenerator;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private ShiftMapper shiftMapper;

    @Autowired
    private PersonService personService;
    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private Validator validator;
    @Autowired
    private ShiftReportGenerator shiftReportGenerator;

    @Autowired
    private MailManager mailManager;


    public Map<String,Integer> validateDate(String date){
        HashMap<String, Integer> resp = new HashMap<>();
        Integer year = null;
        Integer month = null;
        Integer day = null;

        if(date != null){
            String[] splitedDate = date.split("-");
            year = splitedDate.length>0 ? Integer.valueOf(splitedDate[0]) : null;
            month = splitedDate.length>1 ? Integer.valueOf(splitedDate[1]) : null;
            day = splitedDate.length>2 ? Integer.valueOf(splitedDate[2]) : null;
        }

        resp.put("y",year);
        resp.put("m",month);
        resp.put("d",day);
        return resp;
    }
    public ShiftReadDtoArray findAll (Dam_enum dam, String stringDate, Boolean shiftState, Integer byUser, Integer page, Integer size, String sortBy){
        Page<Shift> results;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Map<String, Integer> date = validateDate(stringDate);

        results = shiftRepository.findAllByOptionalParametersAndUser(dam ,shiftState,byUser, date.get("y"), date.get("m"), date.get("d"), pageable);

        if(byUser == null){
            results = shiftRepository.findAllByOptionalParameters(dam ,shiftState, date.get("y"), date.get("m"), date.get("d"), pageable);
        }


        Page pagedResults = results.map(entity -> shiftMapper.toReadDTO(entity));
        return ShiftReadDtoArray.builder()
                .shifts(pagedResults.getContent())
                .total_results(pagedResults.getTotalElements())
                .results_per_page(size)
                .current_page(page)
                .pages(pagedResults.getTotalPages())
                .sort_by(sortBy)
                .build();
    }
    public ShiftReadDto findById (Integer id){
        return shiftMapper.toReadDTO(getShiftById(id));
    }

    public ShiftReadDto findShiftByIdUser (Integer id){
        return shiftMapper.toReadDTO(getShiftByIdUser(id));
    }
    public Shift getShiftByIdUser (Integer id){
        Optional<Shift> shift = shiftRepository.getShiftByIdUser(id);
        if(shift.isEmpty()) throw new NotFoundException("Turno no encontrado con el staff id: "+id);
        return shift.get();
    }

    public Shift getShiftById (Integer id){
        Optional<Shift> shift = shiftRepository.findById(id);
        if(shift.isEmpty()) throw new NotFoundException("Turno no encontrado por id: "+id);
        return shift.get();
    }
    public ShiftReadDto deleteShift (Integer id){
        Shift shiftToDelete = getShiftById(id);
        if (!shiftToDelete.getStaff().isEmpty()) throw new ForbiddenAction("Para eliminar turno, este no puede tener personal cargado!");
        List<RecordReadDto> openRecords = recordService.getOpenRecords(shiftToDelete.getRecords());
        if (!openRecords.isEmpty()) throw new ForbiddenAction("Para eliminar turno, este no puede tener registros abiertos!");
        shiftRepository.deleteById(id);
        return shiftMapper.toReadDTO(shiftToDelete);
    }
    public ShiftReadDto addStaffUser(Integer idShift, StaffMemberAddDto staffAddDto){
        Shift shiftBd = getShiftById(idShift);
        if(shiftBd.getStaff().stream().anyMatch(user -> user.getDni().equals(staffAddDto.getDni()))) throw new AlreadyExistException("Ya existe el usuario dentro del staff");
        User staffMemberEntity = userService.getUserStaffMemberByDni(staffAddDto.getDni());
        shiftBd.getStaff().add(staffMemberEntity);
        shiftRepository.save(shiftBd);
        return shiftMapper.toReadDTO(shiftBd);
    }
    public ShiftReadDto removeStaffFromShift(Integer idShift, Integer idStaff){
        Shift shiftBd = getShiftById(idShift);
        User userToRemove = userService.findById(idStaff);
        Boolean removeResult = shiftBd.getStaff().remove(userToRemove);
        if (!removeResult) throw new NotFoundException("No se encontro miembro de staff con id:"+idStaff);
        shiftRepository.save(shiftBd);
        return  shiftMapper.toReadDTO(shiftBd);
    }
    public ShiftReadDto addNewRecord(RecordAddDto recordAddDTO){
        // validar que shift exista y que este en condiciones de agregar registro
        Shift shiftBd = getShiftById(recordAddDTO.getIdShift());
        if (shiftBd.getClose()) throw new ForbiddenAction("La guardia esta cerrada, es imposible agregar el registro!");

        if( recordAddDTO.getLicense() != null && recordAddDTO.getSimpleBoat() != null) throw new ForbiddenAction( "Los registros pueden tener un bote con matricula o sin matricula, no puedes enviar ambos datos!");

        // obtener registros, verificar que no exista y crear nuevo record, agregarlo al listado y actualizar shift...
        List<Record> records = shiftBd.getRecords();
        if(!records.isEmpty() && recordAddDTO.getHasLicense()) validateNonDuplicatedRecordsByLicense(records, recordAddDTO);  // validar que no hayan registros duplicados...
        Record newRecord = recordService.addNewRecord(recordAddDTO);
        records.add(newRecord);
        shiftBd.setRecords(records);
        return shiftMapper.toReadDTO(shiftRepository.save(shiftBd));
    }
    public RecordReadDto updateRecord(Integer idRecord, RecordUpdateDto recordUpdateDTO){
        Shift shiftBd = getShiftById(recordUpdateDTO.getIdShift()); // obtener shift en bd
        List<Record> recordBdList = shiftBd.getRecords().stream().filter(record -> record.getId() == idRecord).collect(Collectors.toList());
        if (recordBdList.isEmpty()) throw new NotFoundException("El registro a actualzar con id: "+idRecord+", no existe en el turno id: "+recordUpdateDTO.getIdShift()+", verifica los datos!");

        List<Record> activeRecords = shiftBd.getRecords().stream().filter(rec -> rec.getRecordState().equals(RecordState_enum.ACTIVO)).collect(Collectors.toList());
        if (!activeRecords.isEmpty()){ // si hay registros activos
            LicenseUpdateDto licenseToUpdate = recordUpdateDTO.getLicense();
            if(licenseToUpdate != null){
                if(activeRecords.stream().anyMatch(record -> record.getLicense().getLicenseCode().equals(licenseToUpdate.getLicenseCode()))) throw new InvalidValueException("Ya existe un registro ACTIVO con la misma matricula: " + recordUpdateDTO.getLicense().getLicenseCode());
            } else if (recordUpdateDTO.getSimpleBoat() != null){
                // TODO ACA DEBO CREAR UNA FORMA DE VALIDAR QUE NO EXISTE EL MISMO CONJUNTO DE EMBARCACION + PATENTE AUTO + TIPO DE EMBARCACION? O CUAL SERIA LA REGLA DE NEGOCIO NECESARIA?
                // TODO ACA DEBO CREAR UNA FORMA DE VALIDAR QUE NO EXISTE EL MISMO CONJUNTO DE EMBARCACION + PATENTE AUTO + TIPO DE EMBARCACION? O CUAL SERIA LA REGLA DE NEGOCIO NECESARIA?
                // TODO ACA DEBO CREAR UNA FORMA DE VALIDAR QUE NO EXISTE EL MISMO CONJUNTO DE EMBARCACION + PATENTE AUTO + TIPO DE EMBARCACION? O CUAL SERIA LA REGLA DE NEGOCIO NECESARIA?
                // TODO ACA DEBO CREAR UNA FORMA DE VALIDAR QUE NO EXISTE EL MISMO CONJUNTO DE EMBARCACION + PATENTE AUTO + TIPO DE EMBARCACION? O CUAL SERIA LA REGLA DE NEGOCIO NECESARIA?
            }

        }
        return recordService.updateRecord(recordBdList.get(0) ,recordUpdateDTO);
    }

    public ShiftReadDto createShift(ShiftAddDto shiftAddDto){
        List<User> userListToAdd = new ArrayList<>();
        if (shiftAddDto.getStaff() != null){ // si staff tiene users, los busco y los agrego
            List<UserDni> userListDni = shiftAddDto.getStaff();
            userListDni.forEach(userDni ->{
                User userEntity = userService.getUserStaffMemberByDni(userDni.getDni());
                if(userEntity.getRole().name().equals("LIFEGUARD")){
                    userListToAdd.add(userEntity);
                } else{
                    // TODO aca deberia crear un listado de usuarios que no cumplen con la condicion de guardavida y responder que no son gv...
                }
            });
        }

        Shift shiftEntity = shiftMapper.toEntity(shiftAddDto);
        shiftEntity.setStaff(userListToAdd);
        shiftEntity.setDate(LocalDate.now());
        shiftEntity.setClose(false);
        Shift savedShift = shiftRepository.save(shiftEntity);
        return shiftMapper.toReadDTO(savedShift);
    }

    public ShiftReadDto updateShift (Integer idShift, ShiftUpdateDto shiftUpdateDto) throws JRException, IOException {
        Shift  shiftBd = getShiftById(idShift);
        if (shiftUpdateDto.getDam() != null){
            // todo validar dam? dam es validada automaticamente por spring, pero deberia validar algo mas? como que solo puede haber un unico uso de la dam en un dia particular?
            shiftBd.setDam(shiftUpdateDto.getDam());
        }
        if (shiftUpdateDto.getNotes() != null){
            // todo validar notes
            shiftBd.setNotes(shiftUpdateDto.getNotes());
        }
        if (shiftUpdateDto.getClose() != null){
            setCloseStatusShift(shiftBd);
        }
        return shiftMapper.toReadDTO(shiftRepository.save(shiftBd));
    }
    public ShiftReadDto setCloseStatusShift(Shift shiftBd) throws JRException, IOException {
        if (recordService.getOpenRecords(shiftBd.getRecords()).size() > 0){
            throw new ForbiddenAction("Aun existen registros activos, por favor cierra todos los registros!");
        }
        shiftBd.setClose(shiftBd.getClose() == true? false : true);
        shiftRepository.save(shiftBd);

        // TODO ACA DEBO PONER UN LISTADO DE EMAILS, DE LOS ADMIN O JEFES A LOS CUALES MANDARLE EL RESUMEN DE GUARDIA, PODRIA MANDARSELOS A LOS QUE ESTAN DENTRO DEL STAFF
        // TODO ACA DEBO PONER UN LISTADO DE EMAILS, DE LOS ADMIN O JEFES A LOS CUALES MANDARLE EL RESUMEN DE GUARDIA, PODRIA MANDARSELOS A LOS QUE ESTAN DENTRO DEL STAFF
        // TODO ACA DEBO PONER UN LISTADO DE EMAILS, DE LOS ADMIN O JEFES A LOS CUALES MANDARLE EL RESUMEN DE GUARDIA, PODRIA MANDARSELOS A LOS QUE ESTAN DENTRO DEL STAFF
        sendEmailShiftResume(shiftBd.getId() , "davidcst2991@gmail.com");
        return shiftMapper.toReadDTO(shiftBd);
    }


    public void validateNonDuplicatedRecordsByLicense (List<Record> records , RecordAddDto recordAddDTO){
        List<Record> activeRecords = records.stream().filter(rec -> (rec.getRecordState().equals(RecordState_enum.ACTIVO) && rec.getLicense() != null)).collect(Collectors.toList());
        if (!activeRecords.isEmpty()){ // si hay registros activos y que tengan licencia
            if(activeRecords.stream().anyMatch(record -> record.getLicense().getLicenseCode().equals(recordAddDTO.getLicense().getLicenseCode()))) throw new InvalidValueException("Ya existe un registro ACTIVO con la misma matricula: " + recordAddDTO.getLicense().getLicenseCode());
        }
    }


    public byte[] shiftResumePdf(Integer shiftId) throws JRException, IOException {
        return shiftReportGenerator.shiftExportToPdf(getShiftById(shiftId));
    }

    public Boolean sendEmailShiftResume(Integer shiftId, String email) throws JRException, IOException {
        byte[] pdfBytes = shiftResumePdf(shiftId);
        LocalDate date = LocalDate.now();

        File fileToAttach = File.createTempFile("shift_ID_"+shiftId+"_"+date , ".pdf");
        FileOutputStream fileOutput = new FileOutputStream(fileToAttach);
        fileOutput.write(pdfBytes);
        Boolean emailStatus = mailManager.sendEmail(email, "Resumen de guardia con fecha: ", "Este es un resumen enviado automaticamente",fileToAttach).equals("OK");
        fileToAttach.delete();
        return emailStatus;
    }
}
