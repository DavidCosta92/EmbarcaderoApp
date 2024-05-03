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
import java.time.LocalDate;
import java.util.*;

@Service
public class ShiftReportGenerator {
    Integer simpleBoat = 0;
    Integer registeredBoat = 0;
    List<BoatTypeReport> boatTypeList = new ArrayList<>();
    List<BoatTypeLicence> boatLicenceList = new ArrayList<>();
    List<PersonHoursReport> personHoursList = new ArrayList<>();

    // TODO PENDIENTE- Total boat por hora
    // List<PersonHoursReport> totalBoatList = new ArrayList<>();






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
    private FileInputStream getChartDataHoursPersonsBoats() throws FileNotFoundException {
        File chart = ResourceUtils.getFile("classpath:images/chart.png");
        return new FileInputStream(chart);
    }
    private void getChartDataBoatTypesByLicences(Shift shift) {
        List<Record> records = shift.getRecords();
        Map<String, Integer> boatTypes = new HashMap<>();

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
        }
        boatTypes.forEach((type, amount) ->{
            boatTypeList.add(new BoatTypeReport(type, amount));
        });

        // CHART DE MATRICULAS
        boatLicenceList.add(new BoatTypeLicence("Sin matricula",simpleBoat));
        boatLicenceList.add(new BoatTypeLicence("Matriculadas",registeredBoat));

        /*
        let startDate = new Date(shiftById.date).getTime()
        let totalPersons = [{time: startDate,  value: 0}] // inicia en 0
        let realPersons = [{time: startDate,  value: 0}] // inicia en 0
        let recordsDate =[startDate] //inicia en la fecha a la hora 00:00
        let totalActivePersons = [{time: startDate,  value: 0}] // inicia en 0
        let auxData = []
        shiftById.records.forEach((record, index)=>{
            let newDate = new Date(record.startTime).getTime()
            recordsDate.push(newDate)
            totalPersons.push({time: record.startTime , value: getTotalPersonsUntilIndex(index)})
            realPersons.push({time: record.startTime , value: getTotalActivePersonsUntilIndex(index)})
            totalActivePersons.push({time: record.startTime , value: index+1})
        })
        auxData.push(recordsDate)
        auxData.push(realPersons)
        auxData.push(totalPersons)
        auxData.push(totalActivePersons)
        setDataHoursPersonsBoats(auxData)
         */

        // CHART Persons Hours
        LocalDate startTime = shift.getDate();
        Integer totalPersonByTime = 0;
        personHoursList.add( new PersonHoursReport(startTime.toString(), totalPersonByTime));

        Integer totalBoatByTime = 0;
        personHoursList.add( new PersonHoursReport(startTime.toString(), totalBoatByTime));

        for(int i =0; i<shift.getRecords().size(); i++){
            totalPersonByTime += shift.getRecords().get(i).getNumberOfGuests() + 1;
            personHoursList.add( new PersonHoursReport(shift.getRecords().get(i).getStartTime().toString(), totalPersonByTime ));


            // TODO PENDIENTE- Total boat por hora
            // totalBoatList.add( new PersonHoursReport(shift.getRecords().get(i).getStartTime().toString(), totalBoatByTime ));
        }
        /* [
            PersonHoursReport(timePersonHours=2024-05-01, valuePersonHours=0),
            PersonHoursReport(timePersonHours=2024-05-01 12:26:32.088, valuePersonHours=4),
            PersonHoursReport(timePersonHours=2024-05-02 18:52:40.68, valuePersonHours=7),
            PersonHoursReport(timePersonHours=2024-05-02 18:53:13.308, valuePersonHours=17)
        ] */

        System.out.println(personHoursList);
    }
    private FileInputStream getChartDataBoatTypes() throws FileNotFoundException {
        File chart = ResourceUtils.getFile("classpath:images/chart.png");
        return new FileInputStream(chart);
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
        params.put("records", new JRBeanArrayDataSource(shift.getRecords().toArray()));

        // Obtener datos para graficos
        getChartDataBoatTypesByLicences(shift);
        params.put("dataSetBoatTypes",new JRBeanCollectionDataSource(boatTypeList));
        params.put("dataSetBoatLicence",new JRBeanCollectionDataSource(boatLicenceList));

        // - Total personas por hora Personas
        params.put("dataSetPersonHours",new JRBeanCollectionDataSource(personHoursList));

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
