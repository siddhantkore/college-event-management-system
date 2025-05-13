package in.adcet.event_management.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import in.adcet.event_management.dao.EventDAO;
import in.adcet.event_management.dao.UserDAO;
import in.adcet.event_management.entity.Events;
import in.adcet.event_management.entity.User;
import in.adcet.event_management.notifications.MailNotification;
import in.adcet.event_management.notifications.Notification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventService {
	
	private final EventDAO eventDAO = new EventDAO();
	private final UserDAO userDAO = new UserDAO();
	private final Notification mailNotification = new MailNotification();
	
	public boolean addANewEvent (Events event) {
		
		try {
			System.out.println(event);
			eventDAO.addNewEvent(event);
			List<String> emails = userDAO.getAllUser()
											.stream()
											.filter(user -> !user.getRole()
													.equalsIgnoreCase("admin"))
								            .map(User::getEmail)
								            .collect(Collectors.toList());
			
			if(!emails.isEmpty()){
				mailNotification.sendNotificationToAll(emails, event);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void deleteEvent (String code) {
		try {
			String deleteResult = eventDAO.deleteEvent(code);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Events> getAllEvents () {
		log.info("In GetAll Event Method");
		try {
			List<Events> allEvents = eventDAO.getAllEvents();
			if(allEvents==null) {
				throw new Exception();
			}

			 return allEvents;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public Events getEventByEventName (String eventName) {

		try {

			Events event = eventDAO.getEventByName(eventName);
			return event;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

    public Optional<Events> getEventByCode(String code) {

		try {

			Events event = eventDAO.getEventByCode(code);
			return Optional.ofNullable(event);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;

    }

	public Optional<Events> updateEvent(Events updatedEvent) {
		try {
			Optional<Events> existingEventOpt = getEventByCode(updatedEvent.getCode());
			if (existingEventOpt.isPresent()) {
				eventDAO.updateEvent(updatedEvent);  // Persist updated entity
				return Optional.of(updatedEvent);
			} else {
				return Optional.empty();  // Event doesn't exist
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
