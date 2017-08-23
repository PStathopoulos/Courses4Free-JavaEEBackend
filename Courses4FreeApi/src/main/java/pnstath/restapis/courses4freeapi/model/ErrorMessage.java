package pnstath.restapis.courses4freeapi.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage {
        
	
	// ==========================================
        // =                ATTRIBUTES              =
        // ==========================================
	
	
	private String errorMessage;
	private int errorCode;
	private String documentation;
	
	
	// ==========================================
        // =               CONSTRUCTORS             =
        // ==========================================
	
	
	public ErrorMessage() {
		
	}
		
	public ErrorMessage(String errorMessage, int errorCode, String documentation) {
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.documentation = documentation;
	}
	
	
	// ===========================================
        // =             GETTERS & SETTERS           =
        // ===========================================
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getDocumentation() {
		return documentation;
	}
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	
	
}

