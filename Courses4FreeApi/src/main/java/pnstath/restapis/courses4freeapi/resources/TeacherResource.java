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
import pnstath.restapis.courses4freeapi.dtos.TeacherDTO;
import pnstath.restapis.courses4freeapi.exceptions.DataNotFoundException;
import pnstath.restapis.courses4freeapi.exceptions.UnauthorizedUserException;
import pnstath.restapis.courses4freeapi.filters.TeacherAuthWithJWT;
import pnstath.restapis.courses4freeapi.model.Course;
import pnstath.restapis.courses4freeapi.model.Student;
import pnstath.restapis.courses4freeapi.model.Teacher;
import pnstath.restapis.courses4freeapi.services.TeacherService;

@Path("teachers")
@RequestScoped
public class TeacherResource {
	
	
	// =======================================
        // =           INJECTION POINTS          =
        // =======================================
	
	
	@Inject
	private TeacherService teacherService;
	
	
	// =======================================
        // =         ENDPOINT/RESOURCE METHODS   =
        // =======================================
    
	// Fetching all the available teachers
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAllTeachers() {

		// Injected service call
		List<TeacherDTO> teacherDTOs = teacherService.getAllTeachers();

		// Check for null or empty list of students
		if (teacherDTOs == null || teacherDTOs.isEmpty()) {
			throw new DataNotFoundException("The requested teachers cannot be found");
		}

		// Construct and return List<TeacherDTO> on GenericEntity, within the response body	
		GenericEntity<List<TeacherDTO>> genEnt = new GenericEntity<List<TeacherDTO>>(teacherDTOs) {
		};

		return Response.ok(genEnt).build();
	}

	// Fetching a teacher specified by id
	@GET
	@Path("/{teacherId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getTeacherById(@PathParam("teacherId") Long teacherId) {

		// Injected service call
		TeacherDTO teacherDTO = teacherService.getTeacherById(teacherId);

		return Response.ok(teacherDTO).build();
	}

	// Fetching courses of a specified by id teacher
	@GET
	@Path("/{teacherId}/courses")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAllCoursesByTeacher(@PathParam("teacherId") Long teacherId) {

		// Injected service call
		Set<CourseDTO> courseDTOs = teacherService.getCoursesByTeacher(teacherId);

		// Check for null or empty list of courses
		if (courseDTOs == null || courseDTOs.isEmpty()) {
			throw new DataNotFoundException(
					"The requested courses for" + " teacher with id=" + teacherId + " cannot be found");

		}

		// Construct and return Set<CourseDTO> on GenericEntity, within the
		// Response body
		GenericEntity<Set<CourseDTO>> genEnt = new GenericEntity<Set<CourseDTO>>(courseDTOs) {
		};

		return Response.ok(genEnt).build();
	}

	// Fetching a particular course for a specified teacher
	@GET
	@Path("/{teacherId}/courses/{courseId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCourseByTeacher(@PathParam("teacherId") Long teacherId, @PathParam("courseId") Long courseId) {

		// Injected service call
		Course course = teacherService.getCourseByTeacher(teacherId, courseId);

		// Construct Course DTO from Course Entity
		CourseDTO courseDTO = new CourseDTO(course);

		return Response.ok(courseDTO).build();
	}

	// Fetching students of a specified by id teacher
	@GET
	@Path("/{teacherId}/students")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAllStudentsByTeacher(@PathParam("teacherId") Long teacherId) {

		// Injected service call
		Set<StudentDTO> studentDTOs = teacherService.getStudentsByTeacher(teacherId);

		// Check for null or empty list of courses
		if (studentDTOs == null || studentDTOs.isEmpty()) {
			throw new DataNotFoundException(
					"The requested students for" + " teacher with id=" + teacherId + " cannot be found");

		}

		// Construct and return Set<StudentDTO> on GenericEntity, within the response body
		GenericEntity<Set<StudentDTO>> genEnt = new GenericEntity<Set<StudentDTO>>(studentDTOs) {
		};

		return Response.ok(genEnt).build();
	}
	
	// Fetching a particular student for a specified teacher
	@GET
	@Path("/{teacherId}/students/{studentId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getStudentByTeacher(@PathParam("teacherId") Long teacherId, @PathParam("studentId") Long studentId) {

		// Injected service call
		Student student = teacherService.getStudentByTeacher(teacherId, studentId);

		// Construct Student DTO from Student Entity
		StudentDTO studentDTO = new StudentDTO(student);

		return Response.ok(studentDTO).build();
	}

	// Adding teacher to the database
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addTeacher(TeacherDTO teacherDTO, @Context UriInfo uriInfo) {

		// Injected service call
		Teacher teacher = teacherService.addTeacher(teacherDTO, uriInfo);

		// Building the resource uri and return it within response
		// in the location of the header
		String teacherId = String.valueOf(teacher.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(teacherId).build();

		return Response.created(uri).build();
	}

	// Teacher Login operation
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response authenticateTeacher(@FormParam("username") String username, @FormParam("password") String password,
			@Context UriInfo uriInfo) {

		// Injected service call for providing the token
		String token = teacherService.authenticateTeacher(username, password, uriInfo);

		// If token didn't provided means that teacher doesn't exists in the
		// database
		if (token == null) {
			throw new UnauthorizedUserException(
					"Teacher with username " + username + " password " + password + " cannot be found");
		}

		// Return the token on the response header with the Bearer prefix
		return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();
	}

	// Update teacher to the database
	// Only teachers can have access to this operation
	@PUT
	@Path("/{teacherId}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@TeacherAuthWithJWT
	public Response updateTeacher(@PathParam("teacherId") Long teacherId, TeacherDTO teacherDTO) {

		// Injected service call
		Teacher modifiedTeacher = teacherService.updateTeacher(teacherId, teacherDTO);

		// Construct Teacher DTO from modified Teacher Entity
		TeacherDTO modifiedTeacherDTO = new TeacherDTO(modifiedTeacher);

		return Response.ok(modifiedTeacherDTO).build();
	}

	// Add course to a particular teacher
	// Only teachers can have access to this operation
	@PUT
	@Path("/{teacherId}/courses/{courseId}")
	@TeacherAuthWithJWT
	public Response addCourseToTeacher(@PathParam("teacherId") Long teacherId, @PathParam("courseId") Long courseId) {
		teacherService.addCourseToTeacher(teacherId, courseId);

		return Response.ok().build();
	}

	// Delete teacher from the database
	// Only teachers can have access to this operation
	@DELETE
	@Path("/{teacherId}")
	@TeacherAuthWithJWT
	public Response deleteStudent(@PathParam("teacherId") Long teacherId) {

		// Injected service call
		teacherService.deleteTeacher(teacherId);

		return Response.noContent().build();
	}

}
