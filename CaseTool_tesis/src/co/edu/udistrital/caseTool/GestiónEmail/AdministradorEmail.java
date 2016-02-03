package co.edu.udistrital.caseTool.GestiónEmail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import co.edu.udistrital.caseTool.Constantes.CConstantes;

public class AdministradorEmail {

	public void enviarEmail(String destinatario, String asunto, String mensaje) {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication( CConstantes.EMAIL_SENDER, CConstantes.EMAIL_SENDER_PASSWORD);
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(CConstantes.EMAIL_SENDER));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(destinatario));
			message.setSubject(asunto);
			message.setText(mensaje);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

}
