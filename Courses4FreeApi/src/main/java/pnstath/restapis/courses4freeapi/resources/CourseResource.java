package pnstath.restapis.courses4freeapi.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import pnstath.restapis.courses4freeapi.dtos.CourseDTO;
import pnstath.restapis.courses4freeapi.exceptions.DataNotFoundException;
import pnstath.restapis.courses4freeapi.exceptions.FileUploadFailsException;
import pnstath.restapis.courses4freeapi.filters.TeacherAuthWithJWT;
import pnstath.restapis.courses4freeapi.model.Course;
import pnstath.restapis.courses4freeapi.services.CourseService;

@Path("courses")
@RequestScoped
public class CourseResource {
	
	
	// =======================================
        // =           INJECTION POINTS          =
        // =======================================

	@Inject
	private CourseService courseService;
	
	
	// =======================================
        // =         ENDPOINT/RESOURCE METHODS   =
        // =======================================
	
    
	// Fetching all the available courses
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAllCourses() {
		// Injected service call
		List<CourseDTO> courseDTOs = courseService.getAllCourses();
		
		// Check for null or empty list of courses
		if(courseDTOs == null || courseDTOs.isEmpty()){
			throw new DataNotFoundException("Courses cannot be found");
		}
		
		// Construct and return List<CourseDTO> on GenericEntity, within the Response body
		GenericEntity<List<CourseDTO>> genEnt = new GenericEntity<List<CourseDTO>>(courseDTOs) {
		};
		
		return Response.ok(genEnt).build();	
	}
	
	// Fetching all the available courses by the specified category
	@GET
	@Path("/category")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCoursesByCategory(@QueryParam("name") String category) {
		
		// Injected service call
		List<CourseDTO> courseDTOs = courseService.getCoursesByCategory(category);
		
		// Check for null or empty list of courses
		if(courseDTOs == null || courseDTOs.isEmpty()){
			throw new DataNotFoundException("Courses in the category " + category + " cannot be found");
		}
		
		// Construct and return List<CourseDTO> on GenericEntity, within the Response body
		GenericEntity<List<CourseDTO>> genEnt = new GenericEntity<List<CourseDTO>>(courseDTOs) {
		};
		
		return Response.ok(genEnt).build();	
	}
	
	// Fetching all the available courses by the specified subject
	@GET
	@Path("/subject")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCoursesBySubject(@QueryParam("name") String subject) {
		
		// Injected service call
		List<CourseDTO> courseDTOs = courseService.getCoursesBySubject(subject);
		
		// Check for null or empty list of courses
		if(courseDTOs == null || courseDTOs.isEmpty()){
			throw new DataNotFoundException("Courses with subject " + subject + " cannot be found");
		}
		
		// Construct and return List<CourseDTO> on GenericEntity, within the Response body
		GenericEntity<List<CourseDTO>> genEnt = new GenericEntity<List<CourseDTO>>(courseDTOs) {
		};
		
		return Response.ok(genEnt).build();	
	}
	
	// Fetching specific courses in paginated way
	@GET
	@Path("/pagination")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCoursesPaginated(@QueryParam("start") int start, @QueryParam("pageSize") int pageSize) {
		
		// Injected service call
		List<CourseDTO> courses = courseService.getCoursesPaginated(start, pageSize);
		
		// Check for null or empty list of courses
		if(courses == null || courses.isEmpty()){
			throw new DataNotFoundException("The requested courses cannot be found");
		}
		
		// Construct and return List<CourseDTO> on GenericEntity, within the Response body
		GenericEntity<List<CourseDTO>> genEnt = new GenericEntity<List<CourseDTO>>(courses) {
		};
		
		return Response.ok(genEnt).build();	
	}
	
	// Fetching specific page with courses
	@GET
	@Path("/page")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCoursesByPageNumber(@QueryParam("number") int pageNumber, @QueryParam("size") int pageSize) {
		
		// Injected service call
		List <CourseDTO> courseDTOs = courseService.getCoursesByPageNumber(pageNumber, pageSize);
		
		// Check for null or empty list of courses
		if(courseDTOs == null || courseDTOs.isEmpty()){
			throw new DataNotFoundException("The requested courses cannot be found");
		}
		
		// Construct and return List<CourseDTO> on GenericEntity, within the Response body
		GenericEntity<List<CourseDTO>> genEnt = new GenericEntity<List<CourseDTO>>(courseDTOs) {
		};
		return Response.ok(genEnt).build();	
	}
	
	// Fetching specific courses based on search term
	@GET
	@Path("/search")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCoursesByTerm(@QueryParam("q") String term) {
		
		// Injected service call
		List <CourseDTO> courseDTOs = courseService.getCoursesByTerm(term);
		
		// Check for null or empty list of courses
		if(courseDTOs == null || courseDTOs.isEmpty()){
			throw new DataNotFoundException("Course/courses depends on the specified search term cannot be found");
		}
		
		// Construct and return List<CourseDTO> on GenericEntity, within the Response body
		GenericEntity<List<CourseDTO>> genEnt = new GenericEntity<List<CourseDTO>>(courseDTOs) {
		};
		
		return Response.ok(genEnt).build();	
	}
	
	// Fetching specific course by id
	@GET
	@Path("/{courseId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCourseById(@PathParam("courseId") Long courseId) {
		
		// Injected service call
		CourseDTO courseDTO = courseService.getCourseById(courseId);
		
		return Response.ok(courseDTO).build();
	}
	
	// Fetching specific course by title
	@GET
	@Path("/title")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCourseByTitle(@QueryParam("q") String courseTitle) {
		
		// Injected service call
		CourseDTO courseDTO = courseService.getCourseByTitle(courseTitle);
		
		return Response.ok(courseDTO).build();
	}
	
	// Adding course to database
	// Only teachers can have access to this operation
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@TeacherAuthWithJWT
	public Response addCourse(CourseDTO courseDTO, @Context UriInfo uriInfo) {
		
		// Injected service call
		Course course = courseService.addCourse(courseDTO, uriInfo);
		
		// Building the resource uri and return it within response 
        // in the location of the header
		String courseId = String.valueOf(course.getId());	
		URI uri = uriInfo.getAbsolutePathBuilder().path(courseId).build();
		
		return Response.created(uri).build();		
	}
	
	// Uploading course photo to the server
	// Only teachers can have access to this operation
	@POST
	@Path("/uploadphoto/{courseid}")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces(MediaType.TEXT_PLAIN)
	@TeacherAuthWithJWT
	public Response uploadPhoto(	@FormDataParam("file") InputStream fileInputStream,
	        						@FormDataParam("file") FormDataContentDisposition fileMetaData, @PathParam("courseId") Long courseId ) 
	{	
		try 
		{   
			// Injected service call
			courseService.uploadPhoto(fileInputStream, fileMetaData, courseId);
			
		} catch (IOException e) 
		{
			throw new FileUploadFailsException("Error while uploading course photo. Please try again!");
		}
		
		return Response.ok("Photo uploaded successfully!").build();
	}

	// Update course to the database
	// Only teachers can have access to this operation
	@PUT
	@Path("/{courseId}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@TeacherAuthWithJWT
	public Response updateCourse(@PathParam("courseId") Long courseId, CourseDTO courseDTO) {
		
		// Injected service call
		Course modifiedCourse = courseService.updateCourse(courseId, courseDTO);
		
		// Construct Course DTO from modified Course Entity
		CourseDTO modifiedCourseDTO = new CourseDTO(modifiedCourse);
		
		return Response.ok(modifiedCourseDTO).build();
	}
    
	// Delete course from the database
	// Only teachers can have access to this operation
	@DELETE
	@Path("/{courseId}")
	@TeacherAuthWithJWT
	public Response deleteCourse(@PathParam("courseId") Long courseId) {
		
		// Injected service call
		courseService.deleteCourse(courseId);
		
		return Response.noContent().build();
	}
}
