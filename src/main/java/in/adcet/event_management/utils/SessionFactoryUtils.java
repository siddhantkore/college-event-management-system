package in.adcet.event_management.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryUtils {
	private static SessionFactory sessionFactory;
	
	private SessionFactoryUtils() {}
	
	public static SessionFactory getSessionFactory() {
		if(sessionFactory == null) {
			sessionFactory = new Configuration()
										.configure("hibernate.cfg.xml")
										.buildSessionFactory();
			return sessionFactory;
		}
		return sessionFactory;
	}
}
