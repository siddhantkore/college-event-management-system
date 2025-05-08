package in.adcet.event_management.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import in.adcet.event_management.entity.Events;
import in.adcet.event_management.utils.SessionFactoryUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class EventDAO {
	
	private final SessionFactory sessionFactory = SessionFactoryUtils.getSessionFactory();
	
	public void addNewEvent (Events event) throws Exception {
		
		Transaction transaction = null;
		
		try(Session session = sessionFactory.openSession()) {
			
			if(event==null)
				throw new Exception("Event must not be null");
			
			transaction = session.beginTransaction();
			session.persist(event);
			
			transaction.commit();
		
		} catch (Exception e) {
			if(transaction!=null)
				transaction.rollback();
			
			log.error("Error While adding new event");
			throw e;
		}
		
	}
	
	
	public String deleteEvent (String code) throws Exception {
		
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()){
			
			transaction = session.beginTransaction();
			String deleteEventQuery = "DELETE FROM Events e WHERE e.code = :code";
			
			int deletedEvent = session.createQuery(deleteEventQuery)
								.setParameter("code", code)
								.executeUpdate();
			
	    	transaction.commit();
	    	if(deletedEvent<1) {
	    		throw new Exception("No Event Found");
	    	}
	    	
	    	return "Deleted successful!!";
	  
    	} catch (Exception e) {
			if(transaction!=null) 
				transaction.rollback();
			log.error("Error While Deleting Event.");
			throw e;
			
		}
    	
	}
	
	public List<Events> getAllEvents () throws Exception {
		
		String getalleventsQuery = "FROM Events";
		try(Session session = sessionFactory.openSession()) {
			List<Events> allEvents = session.createQuery(getalleventsQuery,Events.class).list();
			if(allEvents==null) {
				throw new Exception("No event found");
			}
			 
			return allEvents;
		} catch (Exception e) {
			log.error("Cannot retrieve all events.");
			throw e;
		}
	}
	
	public Events getEventByName (String eventName) throws Exception {
		
		String findEventQuery = "FROM Events e WHERE e.eventName= :eventName";
		
		try (Session session = sessionFactory.openSession()){
			Events event = session.createQuery(findEventQuery,Events.class)
								.setParameter("eventName", eventName)
								.getSingleResult();
			
			if(event==null)
				throw new Exception("No Event found");
	
			return event;
		} catch (Exception e) {
			log.info("Error occured while finding event "+eventName);
			throw e;
		}
		
	}

	public Events getEventByCode(String code) throws Exception {
		String findEventQuery = "FROM Events e WHERE e.code= :code";

		try (Session session = sessionFactory.openSession()){
			Events event = session.createQuery(findEventQuery,Events.class)
					.setParameter("code", code)
					.getSingleResult();

			if(event==null)
				throw new Exception("No Event found");

			return event;
		} catch (Exception e) {
			log.info("Error occured while finding event "+code);
			throw e;
		}
	}

	public void updateEvent(Events event) throws Exception {
		Transaction txn = null;
		try (Session session = sessionFactory.openSession()) {
			txn = session.beginTransaction();
			session.merge(event);
			txn.commit();
		} catch (Exception e) {
			if (txn != null) txn.rollback();
			log.error("Error occurred while updating event with code " + event.getCode(), e);
			throw e;
		}
	}
}
