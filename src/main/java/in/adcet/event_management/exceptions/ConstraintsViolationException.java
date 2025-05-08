package in.adcet.event_management.exceptions;

public class ConstraintsViolationException extends Exception {

	/**
	 * Will be used when any constraints is violated  i.e repeat email, same event ID (Will be specified) 
	 */
	private static final long serialVersionUID = 1L;
	
	public ConstraintsViolationException() {
		super("constraints violation OR give correct values");
	}
	
	public ConstraintsViolationException(String message) {
		super(message);
	}

}
