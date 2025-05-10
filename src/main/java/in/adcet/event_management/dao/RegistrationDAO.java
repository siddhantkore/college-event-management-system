package in.adcet.event_management.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import in.adcet.event_management.entity.Events;
import in.adcet.event_management.entity.Register;
import in.adcet.event_management.entity.User;
import in.adcet.event_management.utils.SessionFactoryUtils;
import lombok.extern.slf4j.Slf4j;
import in.adcet.event_management.DTO.RegistrationDTO;


@Slf4j
public class RegistrationDAO {

	private SessionFactory sessionFactory = SessionFactoryUtils.getSessionFactory();

	private EventDAO eventDAO = new EventDAO();
	
	public List<RegistrationDTO> getAllRegistrations () throws Exception {
		
		String getRegistrationQuery = "SELECT new RegistrationDTO(u.username, u.email, e.eventName, e.eventDate) FROM User u "
									+ "JOIN u.registers r "
									+ "JOIN r.events e";
		
		try (Session session = sessionFactory.openSession()) {
			List<RegistrationDTO> registrations = session.createQuery(getRegistrationQuery,RegistrationDTO.class).list();
			
			return registrations;
			
		} catch (Exception e) {
			log.error("Cannot retrieve Registrations.",e);
			throw e;
		}
	}
	
	public List<RegistrationDTO> getRegisteredStudentOfAEvent (String eventName) throws Exception {
		
		String queryString = "SELECT new RegistrationDTO(u.username, u.email, e.eventName) FROM User u " +
				"JOIN u.registers r " +
				"JOIN r.events e " +
				"WHERE e.eventName = :eventName";
		
		if(eventName==null)
			throw new Exception("Event name must not be null.");
		
		try (Session session = sessionFactory.openSession()){
			
			List<RegistrationDTO> studentsOfEvent = session.createQuery(queryString,RegistrationDTO.class)
										.setParameter("eventName",eventName)
										.getResultList();
			session.close();
			
			return studentsOfEvent;
			
		} catch (Exception e) {
			log.error("error while getting students of event "+eventName);
			throw e;
		}
		
	}
	
	public List<RegistrationDTO> getEventsRegisteredByAStudent (String username) throws Exception {
		
		String queryString = "SELECT new RegistrationDTO(u.username, u.email, e.code, e.name, e.eventDate, e.status) " +
	               "FROM Register r " +
	               "JOIN r.events e " +
	               "JOIN r.user u " +
	               "WHERE u.username = :username";
		
		if (username == null || username.isEmpty())
		    throw new IllegalArgumentException("Username must not be null or empty");
		
		try (Session session = sessionFactory.openSession()){
			
			List<RegistrationDTO> eventsOfStudents = session.createQuery(queryString,RegistrationDTO.class)
										.setParameter("username",username)
										.getResultList();
			
			if(eventsOfStudents.isEmpty())
				log.warn("No Event found for student "+username);
			
			session.close();
			
			return eventsOfStudents;
			
		} catch (Exception e) {
			log.error("error while getting event of student "+username);
			throw e;
		}
	}
	
	public String registerAEvent(Events events, User user) throws Exception {
		
		Transaction transaction=null;
		Register register = new Register();
		
		try (Session session = sessionFactory.openSession()) {
			
			transaction = session.beginTransaction();
			Events events1 = eventDAO.getEventByCode(events.getCode());
			if(events1.getStatus().equalsIgnoreCase("completed"))
				throw new Exception();
			if(events1.getRegistrationCount()>events1.getMaxParticipant())
				throw new Exception();

			int count = events1.getRegistrationCount()+1;
			events1.setRegistrationCount(count);

			eventDAO.updateEvent(events1);

			register.setEvents(events);
			register.setUser(user);
			session.persist(register);
			
			transaction.commit();
			
			return "Registrered Successfully !";
			
		} catch (Exception e) {
			if(transaction!=null)
				transaction.rollback();
			log.error("Error while registration");
			throw e;	
		}
		
	}
	
}
