package pnstath.restapis.courses4freeapi.resources;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import pnstath.restapis.courses4freeapi.dtos.CourseDTO;
import pnstath.restapis.courses4freeapi.dtos.StudentDTO;
import pnstath.restapis.courses4freeapi.exceptions.DataNotFoundException;
import pnstath.restapis.courses4freeapi.exceptions.UnauthorizedUserException;
import pnstath.restapis.courses4freeapi.filters.StudentAuthWithJWT;
import pnstath.restapis.courses4freeapi.model.Course;
import pnstath.restapis.courses4freeapi.model.Student;
import pnstath.restapis.courses4freeapi.services.StudentService;

@Path("students")
@RequestScoped
public class StudentResource {

	
	// =======================================
    // =           INJECTION POINTS          =
    // =======================================
	
	
	@Inject
	private StudentService studentService;
	
	
	// =======================================
    // =         ENDPOINT/RESOURCE METHODS   =
    // =======================================
	
    
	// Fetching all the available students
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAllStudents() {
		
		// Injected service call
		List<StudentDTO> studentDTOs = studentService.getAllStudents();
		
		// Check for null or empty list of students
		if(studentDTOs == null || studentDTOs.isEmpty()){
			throw new DataNotFoundException("The requested students cannot be found");
		}
		
		// Construct and return List<StudentDTO> on GenericEntity, within the Response body
		GenericEntity<List<StudentDTO>> genEnt = new GenericEntity<List<StudentDTO>>(studentDTOs) {
		};
		
		return Response.ok(genEnt).build();	
	}

	// Fetching a student specified by id
	@GET
	@Path("/{studentId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getStudentById(@PathParam("studentId") Long studentId) {
		
		// Injected service call
		StudentDTO studentDTO = studentService.getStudentById(studentId);
		
		return Response.ok(studentDTO).build();
	}
	
	// Fetching courses of a specified by id student
	@GET
	@Path("/{studentId}/courses")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCoursesByStudent(@PathParam("studentId") Long studentId) {
		
		// Injected service call
				Set<CourseDTO> courseDTOs = studentService.getCoursesByStudent(studentId);
				
		// Check for null or empty list of courses
		if (courseDTOs == null || courseDTOs.isEmpty()) {
			throw new DataNotFoundException(
					"The requested courses for" + " student with id=" + studentId + " cannot be found");

		}
		
		// Construct and return Set<CourseDTO> on GenericEntity, within the Response body
		GenericEntity<Set<CourseDTO>> genEnt = new GenericEntity<Set<CourseDTO>>(courseDTOs) {
		};
		
		return Response.ok(genEnt).build();
	}
	
	// Fetching a particular course for a specified student
	@GET
	@Path("/{studentId}/courses/{courseId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCourseByStudent(@PathParam("studentId") Long studentId, @PathParam("courseId") Long courseId) {
		
		// Injected service call
        Course course = studentService.getCourseByStudent(studentId, courseId);
        
        // Construct Course DTO from Course Entity
        CourseDTO courseDTO = new CourseDTO(course);
        
        return Response.ok(courseDTO).build();
	}
	
	// Adding student to the database
    @POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addStudent(StudentDTO studentDTO, @Context UriInfo uriInfo) {
    	
    	// Injected service call
        Student student = studentService.addStudent(studentDTO, uriInfo);
		
        // Building the resource uri and return it within response 
        // in the location of the header
		String studentId = String.valueOf(student.getId());	
		URI uri = uriInfo.getAbsolutePathBuilder().path(studentId).build();
		
		return Response.created(uri).build();	
	}
    
    // Student Login operation
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateStudent(@FormParam("username") String username,
                                        @FormParam("password") String password,
                                        @Context UriInfo uriInfo) {
    	
    	// Injected service call for providing the token
    	String token = studentService.authenticateStudent(username, password, uriInfo);

    	// If token didn't provided means that student doesn't exists in the database
        if(token == null){
			throw new UnauthorizedUserException(
					"Student with username " + username + " password " + password + " cannot be found");
        }
        
        // Return the token on the response header with the Bearer prefix
        return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();
    }
    
    // Update student to the database
 	// Only students can have access to this operation
	@PUT
	@Path("/{studentId}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@StudentAuthWithJWT
	public Response updateStudent(@PathParam("studentId") Long studentId, StudentDTO studentDTO) {
		
		//Injected service call
		Student modifiedStudent = studentService.updateStudent(studentId, studentDTO);
		
		// Construct Student DTO from modified Student Entity
		StudentDTO modifiedStudentDTO = new StudentDTO(modifiedStudent);
		
		return Response.ok(modifiedStudentDTO).build();
	}
	
	// Add course to a particular student
	// Only students can have access to this operation
	@PUT
	@Path("/{studentId}/courses/{courseId}")
	@StudentAuthWithJWT
	public Response addCourseToStudent(@PathParam("studentId") Long studentId, @PathParam("courseId") Long courseId){
		studentService.addCourseToStudent(studentId, courseId);
		
		return Response.ok().build();
	}
	
	// Delete student from the database
	// Only students can have access to this operation
	@DELETE
	@Path("/{studentId}")
	@StudentAuthWithJWT
	public Response deleteStudent(@PathParam("studentId") Long studentId) {
		
		//Injected service call
		studentService.deleteStudent(studentId);
		
		return Response.noContent().build();
	}
		
}
