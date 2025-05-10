package in.adcet.event_management.notifications;

import java.io.File;
import javax.mail.Message;
import javax.mail.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import in.adcet.event_management.entity.Events;
import in.adcet.event_management.utils.NotificationUtils;
import lombok.extern.slf4j.Slf4j;



// make two functions to send mail to runnerup and to winner

@Slf4j
public class MailNotification implements Notification {

	private  String Path="C:\\Java Lab\\Div A\\Java Project\\event-management\\src\\main\\resources\\greetings.html";
	
	private Session session = NotificationUtils.getMailSession();
	
//	public static void main(String args[]) {
//		MailNotification mailNotification = new MailNotification();
//		List<String> emailList = new ArrayList<String>();
//		emailList.add("siddhskore@gmailcom");
//		mailNotification.sendNotificationToAll(emailList,null);
//	}
	
	@Override
	public void sendNotificationToWinner (String to, String eventName) {
		
		try {
			
			MimeMessage mimeMessage = new MimeMessage(session);
			
			Properties properties= NotificationUtils.geProperties();
			mimeMessage.setFrom(properties.getProperty("from"));
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			File htmlFile = new File(Path);
			String htmlContent = new String(java.nio.file.Files.readAllBytes(htmlFile.toPath()), "UTF-8");
			
			// HTML body part
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
			
			mimeMessage.setSubject("Event");
			log.info("Going to send Message");
			
			Transport.send(mimeMessage);
			log.info("Mail sent successfully !!!");
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void sendNotificationToRunnerUp(String to, String eventName) {
		
		try {
			
			MimeMessage mimeMessage = new MimeMessage(session);
			
			Properties properties = NotificationUtils.geProperties();
			mimeMessage.setFrom(properties.getProperty("from"));
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			File htmlFile = new File(Path);
			String htmlContent = new String(java.nio.file.Files.readAllBytes(htmlFile.toPath()), "UTF-8");
			
			
			MimeMultipart multipart = new MimeMultipart("related");

			// HTML body part
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
			multipart.addBodyPart(htmlPart);

			// Image part
			MimeBodyPart imagePart = new MimeBodyPart();

			// Set multipart to message
			mimeMessage.setContent(multipart);

			mimeMessage.setSubject("Event");
			log.info("going to send mail");
			
			Transport.send(mimeMessage);
			log.info("Mail sent successfully !!!");
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void sendNotificationToAll(List<String> emails, Events events) {
		try {
			MimeMessage mimeMessage = new MimeMessage(session);

			Properties properties = NotificationUtils.geProperties();
			mimeMessage.setFrom(properties.getProperty("from"));

			// Add all recipients from the list
			for (String email : emails) {
				log.info(email);
				mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			}

			// Read the HTML template
			File htmlFile = new File("C:\\Java Lab\\Div A\\Java Project\\Copy\\event-management\\src\\main\\resources\\newevent.html");
			String htmlContent = new String(java.nio.file.Files.readAllBytes(htmlFile.toPath()), "UTF-8");

			// Replace placeholders with actual event details
			htmlContent = htmlContent.replace("[EVENT_NAME]", events.getName());
			htmlContent = htmlContent.replace("[DATE & TIME]", events.getEventDate() + " at " + events.getTime());
			htmlContent = htmlContent.replace("[LOCATION]", events.getVenue());

			MimeMultipart multipart = new MimeMultipart("related");

			// HTML body part
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
			multipart.addBodyPart(htmlPart);

			mimeMessage.setContent(multipart);
			mimeMessage.setSubject("New Event has been added: " + events.getName());

			log.info("Going to send mail to {} recipients", emails.size());

			// Send message
			Transport.send(mimeMessage);

			log.info("Mail sent successfully!!!");

		} catch (Exception e) {
			log.error("Failed to send email notification", e);
			e.printStackTrace();
		}
	}

	public void registerEventMail(String mail, String message){
		try {
			MimeMessage mimeMessage = new MimeMessage(session);

			Properties properties = NotificationUtils.geProperties();
			mimeMessage.setFrom(properties.getProperty("from"));

			// Add recipient
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));

			mimeMessage.setContent(message, "text/html; charset=utf-8");

			mimeMessage.setSubject("Registration Of Event");

			log.info("Sending registration confirmation to: {}", mail);

			// Send message
			Transport.send(mimeMessage);

			log.info("Registration email sent successfully!");

		} catch (Exception e) {
			log.error("Failed to send registration email", e);
			e.printStackTrace();
		}
	}

	public void signupMail(String mail) {
		try {
			MimeMessage mimeMessage = new MimeMessage(session);

			Properties properties = NotificationUtils.geProperties();
			mimeMessage.setFrom(properties.getProperty("from"));

			// Add recipient
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));

			String message = "<html><body><h3>Your account has been created successfully!</h3>"
					+ "<p>Welcome! You can now log in and start using our services.</p></body></html>";

			mimeMessage.setContent(message, "text/html; charset=utf-8");

			mimeMessage.setSubject("Account Created Successfully");

			log.info("Sending account creation confirmation to: {}", mail);

			// Send message
			Transport.send(mimeMessage);

			log.info("Account creation email sent successfully!");

		} catch (Exception e) {
			log.error("Failed to send account creation email", e);
			e.printStackTrace();
		}
	}
}
