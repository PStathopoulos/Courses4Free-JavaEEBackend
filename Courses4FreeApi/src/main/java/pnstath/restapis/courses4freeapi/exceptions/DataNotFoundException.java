package pnstath.restapis.courses4freeapi.exceptions;

    // Exception for a requested resource that can be resolved
	public class DataNotFoundException extends RuntimeException {
	
		private static final long serialVersionUID = 1L;

		
		public DataNotFoundException(String message) {
			super(message);
		}
		
	
}
