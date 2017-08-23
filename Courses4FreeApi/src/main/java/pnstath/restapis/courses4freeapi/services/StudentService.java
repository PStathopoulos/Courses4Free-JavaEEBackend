package pnstath.restapis.courses4freeapi.services;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.time.DateUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import pnstath.restapis.courses4freeapi.dtos.CourseDTO;
import pnstath.restapis.courses4freeapi.dtos.StudentDTO;
import pnstath.restapis.courses4freeapi.exceptions.DataNotFoundException;
import pnstath.restapis.courses4freeapi.model.Course;
import pnstath.restapis.courses4freeapi.model.Link;
import pnstath.restapis.courses4freeapi.model.Student;
import pnstath.restapis.courses4freeapi.model.Teacher;
import pnstath.restapis.courses4freeapi.resources.CourseResource;
import pnstath.restapis.courses4freeapi.resources.StudentResource;
import pnstath.restapis.courses4freeapi.utilities.KeyGenerator;

@Stateless
public class StudentService {
	
	
	// =======================================
        // =           INJECTION POINTS          =
        // =======================================

	@PersistenceContext(unitName = "testrest")
	private EntityManager entityManager;
    	
	@Inject
	private KeyGenerator keyGenerator;
	
	
	// =======================================
        // =          DAO BUSINESS METHODS       =
        // =======================================
    
	// DAO method for accessing all the available students from the database
	public List<StudentDTO> getAllStudents() {
		List<StudentDTO> studentDTOs = new ArrayList<StudentDTO>();
		
		// Execute query in order to find all the available students
		TypedQuery<Student> query = entityManager.createNamedQuery(Student.FIND_ALL, Student.class);
		
		// Convert list of Student Entities to list of DTOs 
		// query.getResultList() returns the Student Entities list first
		for (Student student : query.getResultList()) {
			StudentDTO studentDTO = new StudentDTO(student);
			studentDTOs.add(studentDTO);
		}
				
		return studentDTOs;
	}

	// DAO method for accessing a particular student specified by id	
	public StudentDTO getStudentById(Long studentId) {
		
		// Get student specified by id from the database
		Student student = entityManager.find(Student.class, studentId);
		
		// Checking the existence of the student
		if (student == null) {
			throw new DataNotFoundException("Student with id " + studentId + " cannot be found");
		}
		
		// Convert Student Entity to Student DTO
		StudentDTO studentDTO = new StudentDTO(student);
		
		return studentDTO;
	}
	
	// DAO method for adding a student to the database
	public Student addStudent(StudentDTO studentDTO, UriInfo uriInfo) {
		
		//Convert Student DTO to Student Entity
		Student student = studentDTO.fromDTO(entityManager);
		
		// Set UUID of the student
		String UUid = UUID.randomUUID().toString().replace("-", "");
		student.setUUid(UUid);
		
		// Persist student to the database and execute flush 
		// in order to synchronize the Student object with
		// its state as an entity in the database
		entityManager.persist(student);
		entityManager.flush();
		
		// For HATEOAS: build links of the student to self and course entities
		String studentId = String.valueOf(student.getId());	
		
		// Build the self-link
		String selfUri = uriInfo.getBaseUriBuilder()
				 .path(StudentResource.class)
				 .path(studentId)
				 .build()
				 .toString();
		
		// Build the link to course entities
		String coursesUri = uriInfo.getBaseUriBuilder()
				 .path(StudentResource.class)
				 .path(studentId)
				 .path(CourseResource.class)
				 .build()
				 .toString();
        
		// Add links to the list particular of links
		student.getLinks().add(new Link(selfUri,"self"));
		student.getLinks().add(new Link(coursesUri,"courses"));
		
		entityManager.merge(student);
			
		return student;
	}
	
	// DAO method for student authentication
	public String authenticateStudent(String username, String password, UriInfo uriInfo){
		
		// Query to the database for searching the student by the given credentials
		TypedQuery<Student> query = entityManager.createNamedQuery(Student.FIND_BY_CREDENTIALS, Student.class);
		query.setParameter(1, username);
		query.setParameter(2, password);
		Student student = query.getSingleResult();
		
		// Checking the existence of the student
		if(student == null){
			return null;
		}
		
		// If exists, provide jwt token via call to provideToken method
		String token = provideToken(username, uriInfo);
		
		return token;
		
	}
	
	// Method that provides the jwt token for a particular user existed in the database
	public String provideToken(String username, UriInfo uriInfo){
		// Generate secret key
		Key secretKey = keyGenerator.generateKey();
		
		// Build jwt token
        String jwtToken = Jwts.builder()
        		// set the subject equals to username of the student
                .setSubject(username)
                // set the issuer equals to the absolute path of the resource
                .setIssuer(uriInfo.getAbsolutePath().toString())
                // set the issue date to the current date
                .setIssuedAt(new Date())
                // set the expiration to the date after the current date
                .setExpiration(DateUtils.addDays(new Date(), 1))
                // set the scope to student
	            .claim("scope", "student")
	            // sign the token with secretKey via SignatureAlgorithm HS512
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        
        return jwtToken;
		
	}
    
	// DAO Method for updating the state of the student to the database
	public Student updateStudent(Long studentId, StudentDTO studentDTO) {
		
		// Get student specified by id from the database
		Student found = entityManager.find(Student.class, studentId);
        
		// Checking the existence of the student
		if (found == null) {
			throw new DataNotFoundException("Student with id " + studentId + " cannot be found");
		}
		
		//Convert the Student DTO to Student Entity
		Student student = studentDTO.fromDTO(entityManager);
		
		// Partially updating the found student
		if (student.getFirstName() != null && !student.getFirstName().trim().isEmpty()) {
			found.setFirstName(student.getFirstName());
		}

		if (student.getLastName() != null && !student.getLastName().trim().isEmpty()) {
			found.setLastName(student.getLastName());
		}
		
		if (student.getEmail() != null && !student.getEmail().trim().isEmpty()) {
			found.setEmail(student.getEmail());
		}
		
		if (student.getUsername() != null && !student.getUsername().trim().isEmpty()) {
			found.setUsername(student.getUsername());
		}
		
		if (student.getPassword() != null && !student.getPassword().trim().isEmpty()) {
			found.setPassword(student.getPassword());
		}
		
		if (student.getFavouriteSubject() != null && !student.getFavouriteSubject().trim().isEmpty()) {
			found.setFavouriteSubject(student.getFavouriteSubject());
		}
		
		entityManager.merge(found);
		return found;
	}
    
	// DAO Method for deleting a student from a database
	public void deleteStudent(Long studentId) {
		
		// Get student specified by id from the database
		Student found = entityManager.find(Student.class, studentId);

		// Checking the existence of the student
		if (found == null) {
			throw new DataNotFoundException("Student with id " + studentId + " cannot be found");
		}

		entityManager.remove(found);
	}
    
	// DAO Method for fetching all courses for a particular student
	public Set<CourseDTO> getCoursesByStudent(Long studentId) {
		
		// Get student specified by id from the database
		Student student = entityManager.find(Student.class, studentId);
		
		// Checking the existence of the student
		if (student == null) {
			throw new DataNotFoundException("Student with id " + studentId + " cannot be found");
		}
		
		Set<CourseDTO> courseDTOs = new HashSet<CourseDTO>();
		
		// Convert set of Course Entities to set of Course DTOs 
		// student.getCourses method returns the Course Entities list first
		for(Course course : student.getCourses()){
			CourseDTO courseDTO = new CourseDTO(course);
			courseDTOs.add(courseDTO);
		}
		
		return courseDTOs;
	}
	
	// DAO Method for fetching a course specified by id for a particular student
	public Course getCourseByStudent(Long studentId, Long courseId){
		
		Course found = null;
		
		// Get student specified by id from the database
		Student student = entityManager.find(Student.class, studentId);
		
		// Checking the existence of the student
		if (student == null) {
			throw new DataNotFoundException("Student with id " + studentId + " cannot be found");
		}
		
		// Checking of the course in the course list of student
		for(Course course: student.getCourses()){
			if(course.getId()==courseId){
				found = course;
			}
		}
		
		// If course doesn't exist throw exception
		if (found == null) {
			throw new DataNotFoundException("Course with id " + courseId + " cannot be found");
		}
		
		return found;
	}
    
	// DAO Method for adding a course specified by id to a particular student
	public void addCourseToStudent(Long studentId, Long courseId) {
		
		// Get student specified by id from the database
		Student student = entityManager.find(Student.class, studentId);
		
		// Checking the existence of the student
		if (student == null) {
			throw new DataNotFoundException("Student with id " + studentId + " cannot be found");
		}	
		
		// Get course specified by id from the database
		Course course = entityManager.find(Course.class, courseId);
		
		// Checking the existence of the course
		if (course == null) {
			throw new DataNotFoundException("Course with id " + courseId + " cannot be found");
		}
		
		// Add student to the set of students of this course
		course.getStudents().add(student);	
		
		// Add course to the set of courses of this student
		student.getCourses().add(course);
		
		// Merging student, merges also course to the database
		entityManager.merge(student);	
		
		// Get the teacher of the course
		Teacher teacher = course.getTeacher();
		
		if (teacher != null) {
			// Add this student to the set of students of this teacher
			teacher.getStudents().add(student);

			// Add this teacher to the teachers set of the student
			student.getTeachers().add(teacher);

			// Merging teacher, merges also student to the database
			entityManager.merge(student);
		}
		
	}
}
