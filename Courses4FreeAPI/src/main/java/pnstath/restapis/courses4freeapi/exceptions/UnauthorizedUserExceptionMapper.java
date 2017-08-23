package pnstath.restapis.courses4freeapi.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import pnstath.restapis.courses4freeapi.model.ErrorMessage;

//Mapper For UnauthorizedUserException
@Provider
public class UnauthorizedUserExceptionMapper implements ExceptionMapper<UnauthorizedUserException> {

	@Override
	public Response toResponse(UnauthorizedUserException ex) {
		// Construct and return ErrorMessage in the request body
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 401, "courses4freeapi");
		return Response.status(Status.UNAUTHORIZED)
				.entity(errorMessage)
				.build();
	}
}