package in.adcet.event_management.service;

import java.util.List;

import in.adcet.event_management.DTO.RegistrationDTO;
import in.adcet.event_management.dao.RegistrationDAO;
import in.adcet.event_management.entity.Events;
import in.adcet.event_management.entity.User;

public class RegisterService {
	private RegistrationDAO registrationDAO = new RegistrationDAO();
	
	public List<RegistrationDTO> getAllRegistrations (){
		List<RegistrationDTO> registrationDTO=null;
		try {
			registrationDTO = registrationDAO.getAllRegistrations();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return registrationDTO;
	}
	
	public List<RegistrationDTO> getAllStudentsOfaEvent(String eventName) {
		List<RegistrationDTO> registeredStudents =null;
		try {
			registeredStudents = registrationDAO.getRegisteredStudentOfAEvent(eventName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return registeredStudents;
	}
	
	public List<RegistrationDTO> getEventsOfAStudent(String username) {
		
		List<RegistrationDTO> eventsOfStudents=null;
		try {
			eventsOfStudents = registrationDAO.getEventsRegisteredByAStudent(username);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return eventsOfStudents;
	}
	
	public String registerAEvent (Events event, User user) {
		String result = null;
		try {
			result = registrationDAO.registerAEvent(event, user);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}

	
