package com.Embarcadero.demo.utils.reports;

import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.model.entities.Shift;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShiftReportGenerator {

    public byte[] staffExportToPdf(Shift shift) throws JRException, IOException {
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

    private JasperPrint getReport(Shift shift) throws IOException, JRException {
        Map<String, Object> params = new HashMap<String, Object>();

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

        JasperPrint report = JasperFillManager.fillReport(JasperCompileManager.compileReport(ResourceUtils.getFile("classpath:Shift_resume_A4.jrxml").getAbsolutePath()), params, new JREmptyDataSource());

        return report;
    }
}
