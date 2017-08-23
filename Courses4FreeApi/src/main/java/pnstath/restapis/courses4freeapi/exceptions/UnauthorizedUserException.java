package pnstath.restapis.courses4freeapi.exceptions;

// Exception for Unauthorized Access 
public class UnauthorizedUserException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public UnauthorizedUserException(String message) {
		super(message);
		
	}
	
}
