package pnstath.restapis.courses4freeapi.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import pnstath.restapis.courses4freeapi.dtos.CourseDTO;
import pnstath.restapis.courses4freeapi.exceptions.DataNotFoundException;
import pnstath.restapis.courses4freeapi.model.Course;
import pnstath.restapis.courses4freeapi.model.Link;
import pnstath.restapis.courses4freeapi.resources.CourseResource;

@Stateless
public class CourseService {
	
	
	// =======================================
        // =           INJECTION POINTS          =
        // =======================================

	
	@PersistenceContext(unitName = "testrest")
	private EntityManager entityManager;
	
	
	// =======================================
        // =         DAO BUSINESS METHODS        =
        // =======================================
	
        // DAO method for accessing all the available courses from the database
	public List<CourseDTO> getAllCourses() {
		List<CourseDTO> coursesDTOs = new ArrayList<CourseDTO>();
		
		// Execute query in order to find all the available courses
		TypedQuery<Course> query = entityManager.createNamedQuery(Course.FIND_ALL, Course.class);
		
		// Convert list of Course Entities to list of DTOs 
		// query.getResultList() returns the Course Entities list first
		for(Course course : query.getResultList()){
			CourseDTO courseDTO = new CourseDTO(course);
			coursesDTOs.add(courseDTO);
		}	
		
		return coursesDTOs;		
	}
	
	// DAO method for accessing the available courses by the specified category from the database
	public List<CourseDTO> getCoursesByCategory(String category) {
		List<CourseDTO> coursesDTOs = new ArrayList<CourseDTO>();
		
		// Execute query in order to find all the available courses 
		// by the specified category
		TypedQuery<Course> query = entityManager.createNamedQuery(Course.FIND_BY_CATEGORY, Course.class);
		
		// Convert list of Course Entities to list of DTOs 
		// query.setParameter(1, category).getResultList() returns the Course Entities list first by category
		for(Course course : query.setParameter(1, category).getResultList()){
			CourseDTO courseDTO = new CourseDTO(course);
			coursesDTOs.add(courseDTO);
		}
		
		return coursesDTOs;		
	}
	
	// DAO method for accessing the available courses by the specified subject from the database
	public List<CourseDTO> getCoursesBySubject(String subject) {
		List<CourseDTO> coursesDTOs = new ArrayList<CourseDTO>();
		
		// Execute query in order to find all the available courses 
		// by the specified subject
		TypedQuery<Course> query = entityManager.createNamedQuery(Course.FIND_BY_SUBJECT, Course.class);
		
		// Convert list of Course Entities to list of DTOs
		// query.setParameter(1, subject).getResultList() returns the Course Entities list first by subject
		for (Course course : query.setParameter(1, subject).getResultList()) {
			CourseDTO courseDTO = new CourseDTO(course);
			coursesDTOs.add(courseDTO);
		}
		
		return coursesDTOs;		
	}
	
	// DAO method for accessing the available courses in paginated way
        public List<CourseDTO> getCoursesPaginated(int start, int pageSize) {	
    	List<CourseDTO> coursesDTOs = new ArrayList<CourseDTO>();
    	
    	// Execute query in order count the number of the available courses
		TypedQuery<Long> queryCount = entityManager.createNamedQuery(Course.COUNT_ALL, Long.class);
	    long coursesCount = queryCount.getSingleResult();
		
	    // If number of courses equals to or greater than start position plus specified size of page
	    if(coursesCount >= (start + pageSize)){
			TypedQuery<Course> query = entityManager.createNamedQuery(Course.FIND_ALL, Course.class);
			
			// returns the Courses Entities list first based on start position and limit by page size
			List<Course> courses = query.setMaxResults(pageSize)
					                    .setFirstResult(start)
					                    .getResultList();
			
			// Convert list of Course Entities to list of DTOs 
			for (Course course : courses) {
				CourseDTO courseDTO = new CourseDTO(course);
				coursesDTOs.add(courseDTO);
			}
		
	    }
		
		return coursesDTOs;	
	}
    
        // DAO method for accessing the available courses in paginated way by specified page number
	public List<CourseDTO> getCoursesByPageNumber(int pageNumber, int pageSize) {
		List<CourseDTO> coursesDTOs = new ArrayList<CourseDTO>();
		
		// Execute query in order count the number of the available courses
		TypedQuery<Long> queryCount = entityManager.createNamedQuery(Course.COUNT_ALL, Long.class);
		Long coursesCount = queryCount.getSingleResult();
		
		// Specify the number of pages for the given page size
		int numberOfPages = (int)(coursesCount/pageSize);
        
		// If number of pages is equals to or greater than one, 
		// and number of pages is equals to or greater than the specified page number
		// then return the results for the specified page number
		if (numberOfPages>=1 && numberOfPages>=pageNumber) {
			TypedQuery<Course> query = entityManager.createNamedQuery(Course.FIND_ALL, Course.class);
			
			// returns the Courses Entities list first based on page number and limit by page size
			List<Course> courses = query.setMaxResults(pageSize)
					                    .setFirstResult((pageNumber - 1) * pageSize)
					                    .getResultList();
			
			// Convert list of Course Entities to list of DTOs 
			for (Course course : courses) {
				CourseDTO courseDTO = new CourseDTO(course);
				coursesDTOs.add(courseDTO);
			}
		}

		return coursesDTOs;
	}
	
	// DAO method for accessing the available courses by search term	
	public List<CourseDTO> getCoursesByTerm(String term) {
		List<CourseDTO> coursesDTOs = new ArrayList<CourseDTO>();
		
		// Checks for + sign in the search term and replace it with space
		if (term.contains("+")) {
			term = term.replace("+", " ");
		}
		// Checks for %20 sign in the search term and replace it with space
		if (term.contains("%20")) {
			term = term.replace("%20", " ");
		}
		
		// Execute query in order count the number of the available courses
		TypedQuery<Course> query = entityManager.createNamedQuery(Course.FIND_BY_TERM, Course.class);
		// Return Courses Entities list based on search term and return courses that have the term
		// in any position of their title
		List<Course> courses = query.setParameter("term", "%" + term + "%").getResultList();
        
		// Convert list of Course Entities to list of DTOs 
		for (Course course : courses) {
			CourseDTO courseDTO = new CourseDTO(course);
			coursesDTOs.add(courseDTO);
		}
		
		return coursesDTOs;
		
	}

	// DAO method for accessing a particular course specified by its id	
	public CourseDTO getCourseById(Long courseId) {
		
		// Get course specified by id from the database
		Course course = entityManager.find(Course.class, courseId);
		
		// Checking the existence of the course
		if (course == null) {
			throw new DataNotFoundException("Course with id " + courseId + " cannot be found");
	    }
		
		// Convert Course Entity to CourseDTO
		CourseDTO courseDTO = new CourseDTO(course);
		
		return courseDTO;
	}
	
	// DAO method for accessing a particular course specified by its title
	public CourseDTO getCourseByTitle(String courseTitle){
		
		// Query for getting the course specified by title from the database
		TypedQuery<Course> query = entityManager.createNamedQuery(Course.FIND_BY_TITLE, Course.class);
		Course course = query.setParameter(1, courseTitle).getSingleResult();
		
		// Checking the existence of the course
		if (course == null) {
			throw new DataNotFoundException("Course with title " + courseTitle + " cannot be found");
	    }
		// Convert Course Entity to CourseDTO
		CourseDTO courseDTO = new CourseDTO(course);
				
		return courseDTO;
	}
	
	// DAO method for adding a course to the database
	public Course addCourse(CourseDTO courseDTO, UriInfo uriInfo) {
		
		// Convert Course DTO to Course Entity
		Course course = courseDTO.fromDTO(entityManager);
		
		// Set UUID and creation date of the course
		String UUid = UUID.randomUUID().toString().replace("-", "");	
		course.setUUid(UUid);
		Date dateCreated = new Date();
		course.setDateCreated(dateCreated);
		
		// Persist course to the database and execute flush 
		// in order to synchronize the Course object with
		// its state as an entity in the database
		entityManager.persist(course);
		entityManager.flush();
		
		// For HATEOAS: build the self-link of the course 
		String courseId = String.valueOf(course.getId());	
		String uri = uriInfo.getBaseUriBuilder()
				 .path(CourseResource.class)
				 .path(courseId)
				 .build()
				 .toString();
		
		// Add link to the list of links
		course.getLinks().add(new Link(uri,"self"));
		
		entityManager.merge(course);
		
		return course;
	}
	
	// Method for uploading a photo to the server for a particular course
	public void uploadPhoto(InputStream fileInputStream, FormDataContentDisposition fileMetaData, Long courseId) throws IOException{	
		
		// Checking the existence of the course
		getCourseById(courseId);
		
		// Specify the basic path
		final String BASIC_UPLOAD_PATH = "C://Users/Radiostath/Desktop/courses4freeapi/courses_photos/";
		int read = 0;
		byte[] bytes = new byte[1024];
		
		// Build the absolute path for the specified course
		StringBuilder str = new StringBuilder();
		String coursePath = str.append(BASIC_UPLOAD_PATH).append(String.valueOf(courseId)).toString();
		
		// Save/upload the file to the path
		OutputStream out = new FileOutputStream(new File(coursePath + fileMetaData.getFileName()));
		while ((read = fileInputStream.read(bytes)) != -1) 
		{
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
		
	}
	
	// DAO Method for updating the state of the course to the database
	public Course updateCourse(Long courseId, CourseDTO courseDTO) {
		
		// Get course specified by id from the database
		Course found = entityManager.find(Course.class, courseId);
		
		// Checking the existence of the course
		if (found == null) {
			throw new DataNotFoundException("Course with id " + courseId + " cannot be found");
		}
		
		// Convert Course DTO to Course Entity
		Course course = courseDTO.fromDTO(entityManager);
				
		// Partially updating the course
		if (course.getTitle() != null && !course.getTitle().trim().isEmpty()) {
			found.setTitle(course.getTitle());
		}

		if (course.getDescription() != null && !course.getDescription().trim().isEmpty()) {
			found.setDescription(course.getDescription());
		}

		if (course.getSubject() != null && !course.getSubject().trim().isEmpty()) {
			found.setSubject(course.getSubject());
		}

		if (course.getCategory() != null && !course.getCategory().trim().isEmpty()) {
			found.setCategory(course.getCategory());
		}
		
		if (course.getPhotoUrl() != null && !course.getPhotoUrl().trim().isEmpty()) {
			found.setCategory(course.getPhotoUrl());
		}
		
		entityManager.merge(found);
		
		return found;
	}

	// DAO Method for deleting a course from the database
	public void deleteCourse(Long courseId) {
		
		// Get course specified by id from the database
		Course found = entityManager.find(Course.class, courseId);

		// Checking the existence of the course
		if (found == null) {
			throw new DataNotFoundException("Course with id " + courseId + " cannot be found");
		}
		
		entityManager.remove(found);
	}
	
}
