package in.adcet.event_management.exceptions;


/*
 * Will be used at log in and where user is trying to find with its username
 * thrown when Specified user not found
 */
public class UserNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String message) {
		super(message);
	}
	
	public UserNotFoundException() {
		super("User Not Found");
	}
}
