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
        List<User> staffList = shift.getStaff();
        return JasperExportManager.exportReportToPdf(getReport(staffList));
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

    private JasperPrint getReport(List<User> staffList) throws IOException, JRException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ds", new JRBeanArrayDataSource(staffList.toArray()));

        JasperPrint report = JasperFillManager.fillReport(JasperCompileManager.compileReport(ResourceUtils.getFile("classpath:Shift_resume_A4.jrxml").getAbsolutePath()), params, new JREmptyDataSource());

        return report;
    }
}
