package com.Embarcadero.demo.utils.reports;

import com.Embarcadero.demo.exceptions.customsExceptions.InvalidValueException;
import com.Embarcadero.demo.model.entities.Record;
import com.Embarcadero.demo.model.entities.Shift;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import java.io.*;
import java.util.*;

@Service
public class ShiftReportGenerator {
    Integer simpleBoat = 0;
    Integer registeredBoat = 0;
    List<BoatTypeReport> boatTypeList = new ArrayList<>();
    List<BoatTypeLicence> boatLicenceList = new ArrayList<>();
    List<PersonHoursReport> personHoursList = new ArrayList<>();
    public byte[] shiftExportToPdf(Shift shift) throws JRException, IOException {
        // List<User> staffList = shift.getStaff();
        return JasperExportManager.exportReportToPdf(getReport(shift));
    }
/*
    public byte[] exportToXls(List<Pet> list) throws JRException, FileNotFoundException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        SimpleOutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput(byteArray);
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(getReport(list)));
        exporter.setExporterOutput(output);
        exporter.exportReport();
        output.close();
        return byteArray.toByteArray();
    }

 */
    private FileInputStream getMainIcon() throws FileNotFoundException {
        File mainIcon = ResourceUtils.getFile("classpath:images/mainIcon.png");
        return new FileInputStream(mainIcon);
    }
    private int getTotalPersonByHour(Shift shift, int hour){
        List<Record> records = shift.getRecords();
        int totalPersonByHour = 0;
        // Crear una instancia de Calendar y establecer la fecha
        Calendar calendar = Calendar.getInstance();

        for (int i =0; i<records.size(); i++){
            calendar.setTime(records.get(i).getStartTime());
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            if(hours < hour){
                totalPersonByHour+= records.get(i).getNumberOfGuests()+1;
            }
        }
        return totalPersonByHour;
    }
    private void getChartDataBoatTypesByLicences(Shift shift) {
        List<Record> records = shift.getRecords();
        Map<String, Integer> boatTypes = new HashMap<>();
        int persons = 0;
        int boats = 0;

        for (int i = 0 ; i < records.size() ; i++){
            if(records.get(i).getSimpleBoat() != null){
                simpleBoat++;
                String type = records.get(i).getSimpleBoat().getTypeSimpleBoat().name();
                if(boatTypes.get(type) == null){
                    boatTypes.put(type, 1);
                } else{
                    int amount = boatTypes.get(type);
                    boatTypes.put(type, amount+1);
                }
            } else {
                registeredBoat++;
                String type = records.get(i).getLicense().getRegisteredBoat().getTypeLicencedBoat().name();
                if(boatTypes.get(type) == null){
                    boatTypes.put(type, 1);
                } else{
                    int amount = boatTypes.get(type);
                    boatTypes.put(type, amount+1);
                }
            }
            persons+=records.get(i).getNumberOfGuests()+1; // agregar estado personas por hora
            boats+=1; // agregar estado botes por hora suma 1 por cada registro
            personHoursList.add(new PersonHoursReport(records.get(i).getStartTime(), persons , boats));
        }
        boatTypes.forEach((type, amount) ->{
            boatTypeList.add(new BoatTypeReport(type, amount));
        });
        // CHART DE MATRICULAS
        boatLicenceList.add(new BoatTypeLicence("Sin matricula",simpleBoat));
        boatLicenceList.add(new BoatTypeLicence("Matriculadas",registeredBoat));
    }

    private JasperPrint getReport(Shift shift) throws IOException, JRException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("logoPrincipal", getMainIcon());

        // DIQUE
        String dam = "";
        if(shift.getDam().name() == "DIQUE_ULLUM") dam="Ullum";
        if(shift.getDam().name() == "DIQUE_PUNTA_NEGRA") dam="Punta Negra";
        if(shift.getDam().name() == "DIQUE_CUESTA_DEL_VIENTO") dam="Cuesta del viento";
        params.put("dam", dam);

        // FECHA
        params.put("date", shift.getDate().toString());
        // ESTADO GUARDIA
        String shiftState = "Estado: finalizada";
        if(!shift.getClose()) shiftState = "Estado: NO FINALIZADA";
        params.put("state", shiftState);
        // STAFF
        params.put("staff", new JRBeanArrayDataSource(shift.getStaff().toArray()));
        // RECORDS
        params.put("totalBoats", shift.getTotalBoats());
        params.put("totalPersons", shift.getTotalPersons());

        List<Record> records = shift.getRecords();
        List<RecordDtoReport> reportsDto = new ArrayList<>();


        // todo falta => PROBARRRRRR PROBARRRRR
        // todo falta => PROBARRRRRR PROBARRRRR
        // por cada record, crear un record para jasper, que sean strings y que mande los textos tal cual quiero
        for (int i = 0; i<records.size(); i++){
            Record r = records.get(i);
            String boatType = r.getSimpleBoat() != null ? r.getSimpleBoat().getTypeSimpleBoat().name() : r.getLicense().getRegisteredBoat().getTypeLicencedBoat().name();
            String licenseCode = r.getLicense()!= null ?  r.getLicense().getCode() : "NO";
            reportsDto.add(new RecordDtoReport().builder()
                            .id(r.getId())
                            .startTime(r.getStartTime())
                            .endTime(r.getEndTime())
                            .recordState(r.getRecordState().name())
                            .licenseCode(licenseCode)
                            .boatType(boatType)
                            .personDni(r.getPerson().getDni())
                            .personPhone(r.getPerson().getPhone())
                            .numberOfGuests(r.getNumberOfGuests())
                            .car(r.getCar())
                            .build());
        }
        params.put("records", new JRBeanCollectionDataSource(reportsDto));

        // todo falta => PROBARRRRRR PROBARRRRR
        // todo falta => PROBARRRRRR PROBARRRRR




        // Obtener datos para graficos
        getChartDataBoatTypesByLicences(shift);
        params.put("dataSetBoatTypes",new JRBeanCollectionDataSource(boatTypeList));
        params.put("dataSetBoatLicence",new JRBeanCollectionDataSource(boatLicenceList));

        // - Total personas por hora Personas + boat hours
        params.put("dataSetPersonHours",new JRBeanCollectionDataSource(personHoursList));

        // params.put("dataSetBoats",new JRBeanCollectionDataSource(boatsHoursList));



        // TODO PENDIENTE- Total boat por hora
        // params.put("dataSetTotalBoatHours",new JRBeanCollectionDataSource(totalBoatList));

        try {
            InputStream jrxmlStream = getClass().getResourceAsStream("/Shift_resume_A4.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);
            JasperPrint report = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
            return report;
        } catch (Exception e) {
            throw new InvalidValueException("Error generando reporte: "+e.getMessage());
        }
    }
}
