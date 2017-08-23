package pnstath.restapis.courses4freeapi.exceptions;

// Exception for a failure on file uploading
public class FileUploadFailsException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
    
	public FileUploadFailsException(String message) {
		super(message);
		
	}
	
}
