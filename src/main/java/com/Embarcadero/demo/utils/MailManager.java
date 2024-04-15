package com.Embarcadero.demo.utils;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Log4j2
// @Configuration
// @PropertySource("classpath:email.properties")
public class MailManager {

    @Value("${spring.mail.username}")
    private String sender;
    //private String sender = "davidcst2991@gmail.com";

    // @Value("${spring.mail.password}")
    // private String password;

    @Autowired
    JavaMailSender javaMailSender;

    public String sendEmail (String email, String subject,  String msg, File fileToAttach){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject(subject);
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message , true);

            if(fileToAttach.isFile()){
                mimeMessageHelper.addAttachment(fileToAttach.getName(), fileToAttach);
            }
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(msg);
            mimeMessageHelper.setFrom(sender);
            javaMailSender.send(message);
            return "OK";
        } catch (Exception e){
            log.info("Error enviando email => "+e);
            return "ERROR ENVIANDO EMAIL => "+e;
        }
    }

    public String sendEmail(String email, String subject,  String msg) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject(subject);
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message , true);

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(msg);
            mimeMessageHelper.setFrom(sender);
            javaMailSender.send(message);
            return "OK";
        } catch (Exception e){
            log.info("Error enviando email => "+e);
            // throw new RuntimeException("ERROR ENVIANDO EMAIL");
            return "ERROR ENVIANDO EMAIL => "+e;
        }
    }

    public void sendEmailToRestorePassword(String email, String token) {
        /*String template = String.format("""
                <div style="color:#000000; background-color: #e9f2ff; border:1px solid #b1e1ff; border-radius:10px; width:70%;margin:auto; margin-bottom: 20px; text-align: center;">                    
                     <h3>Has click en el link para restaurar tu contraseña</h3> 
                    <div style="color:#ffffff; background-color:#048d2d; border: 2px solid #ffffff; border-radius:5px; padding: 10px; margin: 30px; ">                        
                        <a href= %s style="color:#ffffff; font-weight: 900; font-size: 1.5em;"><h4>Link para restaurar tu contraseña</h4></a>
                    </div>                         
                    <div style="color:#ffffff; background-color: #ff3d4e; border: 1px solid #ffffff; width:40%; margin:auto; border-radius:10px;">
                     <p>Es valido solo por 24hs.</p>      
                     </div>     
                </div>
                """, "http://localhost:3000/auth/setNewPassword/"+token);

         */
        String template = String.format("""
                <div style=\"color:#000000; background-color: #e9f2ff; border:1px solid #b1e1ff; border-radius:10px; width:70%%;margin:auto; margin-bottom: 20px; text-align: center;\">                    
                     <h3>Has click en el link para restaurar tu contraseña</h3> 
                    <div style=\"color:#ffffff; background-color:#048d2d; border: 2px solid #ffffff; border-radius:5px; padding: 10px; margin: 30px; \">                        
                        <a href= %s style=\"color:#ffffff; font-weight: 900; font-size: 1.5em;\"><h4>Link para restaurar tu contraseña</h4></a>
                    </div>                         
                    <div style=\"color:#ffffff; background-color: #ff3d4e; border: 1px solid #ffffff; width:40%%; margin:auto; border-radius:10px;\">
                     <p>Es valido solo por 24hs.</p>      
                     </div>     
                </div>
                """, "http://localhost:3000/auth/setNewPassword/"+token);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject("Restauracion de password");
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message , true);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setFrom(sender);
            javaMailSender.send(message);
        } catch (Exception e){
            log.info("Email enviando a => "+email);
            throw new RuntimeException("ERROR ENVIANDO EMAIL");
        }
    }
}
