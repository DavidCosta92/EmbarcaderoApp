package com.Embarcadero.demo.utils.reports;

import com.Embarcadero.demo.exceptions.customsExceptions.InvalidValueException;
import com.Embarcadero.demo.model.entities.Shift;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShiftReportGenerator {

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
    private FileInputStream getChartDataBoatTypesByLicences() throws FileNotFoundException {
        File chart = ResourceUtils.getFile("classpath:images/chart.png");
        return new FileInputStream(chart);
    }
    private FileInputStream getChartDataBoatTypes() throws FileNotFoundException {
        File chart = ResourceUtils.getFile("classpath:images/chart.png");
        return new FileInputStream(chart);
    }


    private JasperPrint getReport(Shift shift) throws IOException, JRException {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logoPrincipal", getMainIcon());
        // todo agregar a jasper report params.put("chartDataHoursPersonsBoats", getChartDataHoursPersonsBoats());
        // todo agregar a jasper report params.put("chartDataBoatTypesByLicences", getChartDataBoatTypesByLicences());
        // todo agregar a jasper report params.put("chartDataBoatTypes", getChartDataBoatTypes());

        // reportes
        // - Total personas por hora Personas
        // - Tipos embarcaciones
        // - - Con matriculas / sin matriculas
        // - - Tipos embarcaciones (sup, kayak, ecys)
        // -

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
