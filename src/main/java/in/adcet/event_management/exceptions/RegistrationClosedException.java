package in.adcet.event_management.exceptions;

public class RegistrationClosedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public RegistrationClosedException() {
		super("Registrations Closed OR The event is Over !");
	}
	

}
