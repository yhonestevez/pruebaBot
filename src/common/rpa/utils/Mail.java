/**
 * 
 */
package common.rpa.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

/**
 * @author yeestevez
 *
 */
public class Mail {
	private static Logger logger = Logger.getLogger(Mail.class);
	/**
	 * Este metodo puede  enviar correos usando una cuenta de gmail
	 * @param mailSubject
	 * @param message
	 */
	public void sendMail(String mailSubject,String message,String receivingMail) {
		
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.port", "587");
      
        
        Session sesion = Session.getDefaultInstance(properties);
        String senderMail = GetProperties.getInstance().getdataProperties("senderMail");
        String password = GetProperties.getInstance().getdataProperties("password");
                       
        MimeMessage mail = new MimeMessage(sesion);
        try {
            mail.setFrom(new InternetAddress (senderMail));
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress (receivingMail));
            mail.setSubject(mailSubject);
            mail.setText(message);
            
            Transport transportar = sesion.getTransport("smtp");
            transportar.connect(senderMail,password);
            transportar.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));          
            transportar.close();
            logger.info("Correo enviado");
                      
            
        } catch (AddressException ex) {
        	logger.error(ex);
        } catch (MessagingException ex2) {
        	logger.error(ex2);
        }
        

	}

}
