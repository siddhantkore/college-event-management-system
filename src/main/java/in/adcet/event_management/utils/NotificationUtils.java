package in.adcet.event_management.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationUtils {

	private static Properties properties1 = null;
	private static Session session = null;

	private NotificationUtils() {}

	public static Session getMailSession() {
		if (session == null) {
			mailUtil();
		}
		return session;
	}

	public static Properties geProperties() {
		if (properties1 == null) {
			mailUtil();
		}
		return properties1;
	}

	private static void mailUtil() {
		Properties properties = new Properties();

		try {
			// Load application.properties from classpath
			properties.load(NotificationUtils.class.getClassLoader().getResourceAsStream("application.properties"));

			log.info("Properties file read successfully");

			Properties mailProperties = new Properties();
			mailProperties.put("mail.smtp.host", properties.getProperty("mail.smtp.host"));
			mailProperties.put("mail.smtp.port", properties.getProperty("mail.smtp.port"));
			mailProperties.put("mail.smtp.starttls.enable", properties.getProperty("mail.smtp.starttls.enable"));
			mailProperties.put("mail.smtp.auth", properties.getProperty("mail.smtp.auth"));

			log.info("Mail properties prepared");

			// Create Session with authenticator
			Session session1 = Session.getInstance(mailProperties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
							properties.getProperty("from"),
							properties.getProperty("password")
					);
				}
			});

			// Store loaded properties and session for reuse
			properties1 = properties;
			session = session1;

		} catch (Exception e) {
			log.error("Failed to load mail properties", e);
			e.printStackTrace();
		}
	}
}
/*
@Slf4j
public class NotificationUtils {
	
	private static Properties properties1=null;
	private static Session session=null;
	
	private NotificationUtils() {}
	
	public static Session getMailSession() {
		if(session==null) {
			mailUtil();
			return session;
		}
		return session;	
	}
	
	public static Properties geProperties () {
		if(properties1==null) {
			mailUtil();
			return properties1;
		}
		return properties1;	
	}
	
	private static void mailUtil () {
			
		Properties properties = new Properties();
		
		try {
			
			properties.load(NotificationUtils.class.getClassLoader().getResourceAsStream("application.properties"));
				
			log.info("properties file read successful");
				
			Properties systemProperties = System.getProperties();
				
			systemProperties.put("mail.smtp.host", properties.getProperty("host"));
			systemProperties.put("mail.smtp.port", properties.getProperty("port"));
			systemProperties.put("mail.smtp.starttls.enable", properties.getProperty("mail.smtp.starttls.enable"));
			systemProperties.put("mail.smtp.auth", properties.getProperty("mail.smtp.auth"));
				
			log.info("below properties");
				
			Session session1 = Session.getInstance(systemProperties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication () {
					return new PasswordAuthentication(properties.getProperty("from"), properties.getProperty("password"));
				}
			});
				
			properties1 = properties;
			session = session1;
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
*/