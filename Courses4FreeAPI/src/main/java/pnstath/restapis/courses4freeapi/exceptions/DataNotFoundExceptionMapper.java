package pnstath.restapis.courses4freeapi.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import pnstath.restapis.courses4freeapi.model.ErrorMessage;

// Mapper For DataNotFoundException
@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

	@Override
	public Response toResponse(DataNotFoundException ex) {
		// Construct and return ErrorMessage in the request body
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 404, "courses4freeapi");
		return Response.status(Status.NOT_FOUND)
				.entity(errorMessage)
				.build();
	}

}
