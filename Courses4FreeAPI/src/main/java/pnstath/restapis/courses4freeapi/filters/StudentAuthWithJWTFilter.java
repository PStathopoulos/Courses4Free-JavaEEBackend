package pnstath.restapis.courses4freeapi.filters;

import java.io.IOException;
import java.security.Key;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import pnstath.restapis.courses4freeapi.exceptions.UnauthorizedUserException;
import pnstath.restapis.courses4freeapi.utilities.KeyGenerator;


//Security Filter that prevents from unauthorized access of a resource
@Provider
@StudentAuthWithJWT
@Priority(Priorities.AUTHENTICATION)
public class StudentAuthWithJWTFilter implements ContainerRequestFilter {

    // ======================================
    // =          INJECTION POINTS          =
    // ======================================

    @Inject
    private KeyGenerator keyGenerator;

    // ======================================
    // =          BUSINESS METHODS          =
    // ======================================

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException{

        // Extract the HTTP Authorization Header from the incoming request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        

        // Check if the HTTP Authorization Header is present and formatted correctly-in JWT way,
        // i.e. starting with 'Bearer'
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            
            throw new UnauthorizedUserException("Authorization header must be provided and started with Bearer");
        }

        // Extract the token from the HTTP Authorization Header 
        // and trim the spaces in front and back of the 'Bearer' string
        String jwtToken = authorizationHeader.substring("Bearer".length()).trim();

		try {

			// Validate the previously extracted token
			Key secretKey = keyGenerator.generateKey();	
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            
			// Get the scope of the jwt token
			String scope = (String) claims.getBody().get("scope");

			// If scope not equals to "teacher" throw an Exception
			if (!scope.equals("student")) {
				throw new Exception();
			}

		} catch (Exception e) {
			// Abort operation for Unauthorized Student
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}
}


