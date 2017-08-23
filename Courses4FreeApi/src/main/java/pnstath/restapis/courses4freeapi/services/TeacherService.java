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
import pnstath.restapis.courses4freeapi.dtos.TeacherDTO;
import pnstath.restapis.courses4freeapi.exceptions.DataNotFoundException;
import pnstath.restapis.courses4freeapi.model.Course;
import pnstath.restapis.courses4freeapi.model.Link;
import pnstath.restapis.courses4freeapi.model.Student;
import pnstath.restapis.courses4freeapi.model.Teacher;
import pnstath.restapis.courses4freeapi.resources.CourseResource;
import pnstath.restapis.courses4freeapi.resources.StudentResource;
import pnstath.restapis.courses4freeapi.resources.TeacherResource;
import pnstath.restapis.courses4freeapi.utilities.KeyGenerator;


@Stateless
public class TeacherService {
	
	
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
	public List<TeacherDTO> getAllTeachers() {
		List<TeacherDTO> teacherDTOs = new ArrayList<TeacherDTO>();
		
		// Execute query in order to find all the available teachers
		TypedQuery<Teacher> query = entityManager.createNamedQuery(Teacher.FIND_ALL, Teacher.class);
		
		// Convert list of Course Entities to list of DTOs 
		// query.getResultList() returns the Student Entities list first	
		for (Teacher teacher : query.getResultList()) {
			TeacherDTO teacherDTO = new TeacherDTO(teacher);
			teacherDTOs.add(teacherDTO);
		}
				
		return teacherDTOs;
	
	}
	
	// DAO method for accessing a particular student specified by id	
	public TeacherDTO getTeacherById(Long teacherId) {
		// Get student specified by id from the database
		Teacher teacher = entityManager.find(Teacher.class, teacherId);
		
		// Get student specified by id from the database
		// Checking the existence of the student
		if (teacher == null) {
			throw new DataNotFoundException("Teacher with id " + teacherId + " cannot be found");
		}
		
		// Convert Teacher Entity to Teacher DTO
		TeacherDTO teacherDTO = new TeacherDTO(teacher);
				
		return teacherDTO;
	}
	
	// DAO method for adding a teacher to the database
	public Teacher addTeacher(TeacherDTO teacherDTO,  UriInfo uriInfo) {
		
		// Convert Teacher DTO to Teacher Entity
		Teacher teacher = teacherDTO.fromDTO(entityManager);
		
		// Set UUID of the student
		String UUid = UUID.randomUUID().toString().replace("-", "");
		teacher.setUUid(UUid);

		// Persist teacher to the database and execute flush
		// in order to synchronize the Teacher object with
		// its state as an entity in the database
		entityManager.persist(teacher);
		entityManager.flush();

		// For HATEOAS: build links of the teacher to self, course entities and student entities
		String teacherId = String.valueOf(teacher.getId());

		// Build the self-link
		String selfUri = uriInfo.getBaseUriBuilder().path(TeacherResource.class).path(teacherId).build().toString();

		// Build the link to course entities
		String coursesUri = uriInfo.getBaseUriBuilder().path(TeacherResource.class).path(teacherId)
				.path(CourseResource.class).build().toString();
		
		// Build the link to student entities
		String studentsUri = uriInfo.getBaseUriBuilder().path(TeacherResource.class).path(teacherId)
				.path(StudentResource.class).build().toString();
		
		// Add links to the list particular of links
		teacher.getLinks().add(new Link(selfUri, "self"));
		teacher.getLinks().add(new Link(coursesUri, "courses"));
		teacher.getLinks().add(new Link(studentsUri, "students"));

		entityManager.merge(teacher);

		return teacher;
	}
	
	// DAO method for student authentication
		public String authenticateTeacher(String username, String password, UriInfo uriInfo){
			
			// Query to the database for searching the student by the given credentials
			TypedQuery<Teacher> query = entityManager.createNamedQuery(Teacher.FIND_BY_CREDENTIALS, Teacher.class);
			query.setParameter(1, username);
			query.setParameter(2, password);
			Teacher teacher = query.getSingleResult();
			
			// Checking the existence of the student
			if(teacher == null){
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
	                // set the scope to teacher
		            .claim("scope", "teacher")
		            // sign the token with secretKey via SignatureAlgorithm HS512
	                .signWith(SignatureAlgorithm.HS512, secretKey)
	                .compact();
	        
	        return jwtToken;
			
		}

	// DAO Method for updating the state of the teacher to the database
	public Teacher updateTeacher(Long teacherId, TeacherDTO teacherDTO) {
		
		// Get teacher specified by id from the database
		Teacher found = entityManager.find(Teacher.class, teacherId);
        
		// Checking the existence of the teacher
		if (found == null) {
			throw new DataNotFoundException("Teacher with id " + teacherId + " cannot be found");
		}
		
		//Convert the Teacher DTO to Teacher Entity
		Teacher teacher = teacherDTO.fromDTO(entityManager);

		// Partially updating the found student
		if (teacher.getFirstName() != null && !teacher.getFirstName().trim().isEmpty()) {
			found.setFirstName(teacher.getFirstName());
		}

		if (teacher.getLastName() != null && !teacher.getLastName().trim().isEmpty()) {
			found.setLastName(teacher.getLastName());
		}
		
		if (teacher.getEmail() != null && !teacher.getEmail().trim().isEmpty()) {
			found.setEmail(teacher.getEmail());
		}
		
		if (teacher.getUsername() != null && !teacher.getUsername().trim().isEmpty()) {
			found.setUsername(teacher.getUsername());
		}
		
		if (teacher.getPassword() != null && !teacher.getPassword().trim().isEmpty()) {
			found.setPassword(teacher.getPassword());
		}
		
		if (teacher.getYearsExperience() != null) {
			found.setYearsExperience(teacher.getYearsExperience());
		}

		entityManager.merge(found);
		return found;
	}
    
	// DAO Method for deleting a teacher from a database
	public void deleteTeacher(Long teacherId) {
		
		// Get student specified by id from the database
		Teacher teacher = entityManager.find(Teacher.class, teacherId);

		// Checking the existence of the teacher
		if (teacher == null) {
			throw new DataNotFoundException("Teacher with id " + teacherId + " cannot be found");
		}

		entityManager.remove(teacher);
	}
    
	// DAO Method for fetching all courses for a particular teacher
	public Set<CourseDTO> getCoursesByTeacher(Long teacherId) {
		
		// Get teacher specified by id from the database
		Teacher teacher = entityManager.find(Teacher.class, teacherId);
		
		// Checking the existence of the student
		if (teacher == null) {
			throw new DataNotFoundException("Teacher with id " + teacherId + " cannot be found");
		}
		
        Set<CourseDTO> courseDTOs = new HashSet<CourseDTO>();
		
		// Convert set of Course Entities to set of Course DTOs 
		// student.getCourses method returns the Course Entities list first
		for(Course course : teacher.getCourses()){
			CourseDTO courseDTO = new CourseDTO(course);
			courseDTOs.add(courseDTO);
		}
		
		return courseDTOs; 
	}
	
	// DAO Method for fetching a course specified by id for a particular student
	public Course getCourseByTeacher(Long teacherId, Long courseId){
		
		Course found = null;
		
		// Get teacher specified by id from the database
		Teacher teacher = entityManager.find(Teacher.class, teacherId);
		

		// Checking the existence of the teacher
		if (teacher == null) {
			throw new DataNotFoundException("Teacher with id " +teacherId + " cannot be found");
		}
		
		// Checking of the course in the course list of teacher
		for(Course course: teacher.getCourses()){
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
	
	// DAO Method for adding a course specified by id to a particular teacher
	public void addCourseToTeacher(Long teacherId, Long courseId) {
		
		// Get teacher specified by id from the database
		Teacher teacher = entityManager.find(Teacher.class, teacherId);

		// Checking the existence of the teacher
		if (teacher == null) {
			throw new DataNotFoundException("Teacher with id " + teacherId + " cannot be found");
		}

		// Get course specified by id from the database
		Course course = entityManager.find(Course.class, courseId);

		// Checking the existence of the course
		if (course == null) {
			throw new DataNotFoundException("Course with id " + courseId + " cannot be found");
		}

		// Set teacher to course
		course.setTeacher(teacher);

		// Add course to the set of courses of this teacher
		teacher.getCourses().add(course);

		// Merging teacher, merges also course to the database
		entityManager.merge(teacher);
		
	}
	
	// DAO Method for fetching students for a particular teacher
	public Set<StudentDTO> getStudentsByTeacher(Long teacherId) {
		
		// Get teacher specified by id from the database
		Teacher teacher = entityManager.find(Teacher.class, teacherId);
		
		// Checking the existence of the teacher
		if (teacher == null) {
			throw new DataNotFoundException("Teacher with id " + teacherId + " cannot be found");
		}
		
        Set<StudentDTO> studentDTOs = new HashSet<StudentDTO>();
		
		// Convert set of Course Entities to set of Course DTOs 
		// student.getCourses method returns the Course Entities list first
		for(Student student : teacher.getStudents()){
			StudentDTO studentDTO = new StudentDTO(student);
			studentDTOs.add(studentDTO);
		}
		
		return studentDTOs; 	
	}
	
	// DAO Method for fetching a student specified by id for a particular teacher
	public Student getStudentByTeacher(Long teacherId, Long studentId){
		
		Student found = null;
		
		// Get teacher specified by id from the database
		Teacher teacher = entityManager.find(Teacher.class, teacherId);
		
		// Checking the existence of the teacher
		if (teacher == null) {
			throw new DataNotFoundException("Teacher with id " + teacherId + " cannot be found");
		}
		
		// Checking of the course in the course list of student
		for(Student student: teacher.getStudents()){
			if(student.getId() == studentId){
				found = student;
			}
		}
		
		// If student doesn't exist throw exception
		if (found == null) {
			throw new DataNotFoundException("Student with id " + studentId + " cannot be found");
		}
		
		return found;
	}
	
	
}
