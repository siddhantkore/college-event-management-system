package in.adcet.event_management.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import in.adcet.event_management.DTO.ResultDTO;
import in.adcet.event_management.entity.*;
import in.adcet.event_management.notifications.MailNotification;
import in.adcet.event_management.notifications.Notification;
import in.adcet.event_management.utils.SessionFactoryUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ResultDAO {
	// there is a problem --> how do admin manually add result and it should map user and event
	// possible solution --> admin select event(name) -> find that event -> admin select username (winner)
	//						-> find student with username -> map in result
	
	// how can Result DTO be used here
	
	private SessionFactory sessionFactory = SessionFactoryUtils.getSessionFactory();
	private EventDAO eventDAO = new EventDAO();
	private UserDAO userDAO = new UserDAO();
	
	public void addAEventResult(String runnerUsername, String winnerUsername, String eventName) throws Exception {
		Transaction transaction = null;
		
		try (Session session = sessionFactory.openSession()) {
			
			Events events = eventDAO.getEventByName(eventName);
			User runnerUpUser = userDAO.getUserByUsername(runnerUsername);
			User winnerUser = userDAO.getUserByUsername(winnerUsername);
			
			transaction = session.beginTransaction();
			
			Results results = new Results();
			results.setEvents(events);
			results.setWinnerUser(winnerUser);
			results.setRunnerUpUser(runnerUpUser);
			
			session.persist(results);
			transaction.commit();
			
			Notification notificationToWinner = new MailNotification();
			notificationToWinner.sendNotificationToWinner(winnerUser.getEmail(), eventName); // send mail as winner declared;
			notificationToWinner.sendNotificationToRunnerUp(runnerUpUser.getEmail(), eventName); // send mail to runnerup
			
		} catch (Exception e) {
			if (transaction!=null)
				transaction.rollback();
				
			log.error("Can't add winner");
			throw e;
		}
		
	}
	
	public List<ResultDTO> getResults() throws Exception {
		
		Transaction transaction = null;
		
		try (Session session = sessionFactory.openSession()) {
			
			String getResultQuery = "SELECT new ResultDTO(winner.username, winner.name, e.eventName, winner.username, runner.username) "
					+ "FROM Results r "
					+ "JOIN r.winnerUser winner "
					+ "JOIN r.runnerUpUser runner "
					+ "JOIN r.events e";
			
			transaction = session.beginTransaction();
			
			List<ResultDTO> results = session.createQuery(getResultQuery,ResultDTO.class)
												.getResultList();
			transaction.commit();
			
			if (results == null || results.isEmpty())
	            log.warn("No results found in the database.");
	            
			return results;
		} catch (Exception e) {
			if(transaction!=null)
				transaction.rollback();
			log.error("Cannot retrieve events");
			throw e;
		}
    	
	}
	
	
}
