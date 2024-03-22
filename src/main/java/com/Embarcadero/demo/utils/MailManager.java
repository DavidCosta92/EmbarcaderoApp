package com.Embarcadero.demo.utils;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class MailManager {

    @Value("${spring.mail.username}")
    private String sender;
    //private String sender = "davidcst2991@gmail.com";

    @Autowired
    JavaMailSender javaMailSender;

    public String sendEmail(String email, String subject,  String msg) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject(subject);
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message , true);
            // File file = new File();
            // mimeMessageHelper.addAttachment("Mi archivo a enviar", file);
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
        // TODO Solo para testing. SE ENVIA TOKEN PARA LUEGO HACER POST CON PASSWORD, Sin embargo, esto deberia estar siendo recibido por email, con un link a un front que obtenga passwords y haga post a el ednpoint enviando el token.

        String template = """
                <div style="color:#000000; background-color: #e9f2ff; border:1px solid #b1e1ff; border-radius:10px; padding: 10px; text-align: center;">                    
                     <h3>ESTE ES EL TOKEN PARA RESTAURAR TU CONTRASEÑA</h3> 
                    <div style="color:#ffffff; background-color:#664d03; border: 1px solid #ffffff; border-radius:5px; padding: 10px; margin: 30px; font-weight: .900;">
                        <h3>%s</h3>   
                    </div>                         
                    <div style="color:#ffffff; background-color: #ff3d4e; border: 1px solid #ffffff; padding: 10px; font-weight: 600;">
                         <h3>Solo para testing. SE ENVIA TOKEN PARA LUEGO HACER POST CON PASSWORD, Sin embargo, esto deberia estar siendo recibido por email, con un link a un front que obtenga passwords y haga post a el ednpoint enviando el token.</h3>     
                        </div>     
                     <p>Es valido solo por 24hs.</p>      
                </div>
                """.formatted(token);


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
