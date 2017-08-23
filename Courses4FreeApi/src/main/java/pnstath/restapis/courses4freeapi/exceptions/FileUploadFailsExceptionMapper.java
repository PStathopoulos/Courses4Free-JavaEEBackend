package pnstath.restapis.courses4freeapi.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import pnstath.restapis.courses4freeapi.model.ErrorMessage;

// Mapper For FileUploadFailsException
@Provider
public class FileUploadFailsExceptionMapper implements ExceptionMapper<FileUploadFailsException> {

	@Override
	public Response toResponse(FileUploadFailsException ex) {
		// Construct and return ErrorMessage in the request body
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 500, "courses4freeapi");
		return Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(errorMessage)
				.build();
	}
}
