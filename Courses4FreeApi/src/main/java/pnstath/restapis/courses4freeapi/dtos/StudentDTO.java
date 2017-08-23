package pnstath.restapis.courses4freeapi.dtos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.oxm.annotations.XmlReadOnly;
import org.hibernate.validator.constraints.NotBlank;

import pnstath.restapis.courses4freeapi.model.Course;
import pnstath.restapis.courses4freeapi.model.Link;
import pnstath.restapis.courses4freeapi.model.Student;
import pnstath.restapis.courses4freeapi.model.Teacher;

@XmlRootElement
public class StudentDTO implements Serializable{
	
	
	// ==========================================
        // =                CONSTANTS               =
        // ==========================================
	
	
	private static final long serialVersionUID = 1L;	
	
	
	// ==========================================
        // =                ATTRIBUTES              =
        // ==========================================
	
	
	private Long id;
	private String UUid;
	@NotBlank(message = "The First Name must not be blank")
	private String firstName;
	@NotBlank(message = "The Last Name must not be blank")
	private String lastName;
	@Pattern(regexp="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "This is not a valid email adress")
        private String email;
        @Size (min = 3, max = 14, message = "Username must contain at least 3 number and at most 14 number of characters")
	private String username;
        @XmlElement
        @XmlReadOnly
        @Pattern(regexp="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})", 
        message = "Password must have at least 6 characters and at most 20, must contain lowercase and uppercase letters, "
    		+ "digits from 0 to 9, one special symbols in the list '@#$%' ")
	private String password;
        private String favouriteSubject;
	
        private Set<CourseDTO> courses = new HashSet<CourseDTO>();	
        private Set<TeacherDTO> teachers = new HashSet<TeacherDTO>();
	private Set<LinkDTO> links = new HashSet<LinkDTO>();
	
	
	// ==========================================
        // =                CONSTRUCTORS            =
        // ==========================================
	
	
	public StudentDTO(){		
	}
	
	// Convert Entity to DTO via Constructor
	public StudentDTO(Student studentEntity) {
		if (studentEntity != null) {
			this.id = studentEntity.getId();
			this.UUid = studentEntity.getUUid();
			this.firstName = studentEntity.getFirstName();
			this.lastName = studentEntity.getLastName();
			this.email = studentEntity.getEmail();
			this.username = studentEntity.getUsername();
			this.password = studentEntity.getPassword();
			this.favouriteSubject = studentEntity.getFavouriteSubject();
		    	
		    for(Course courseEntity : studentEntity.getCourses()){
		    	CourseDTO courseDTO = new CourseDTO(courseEntity);
		    	courses.add(courseDTO);
		    }
		    
		    for(Teacher teacherEntity : studentEntity.getTeachers()){
		    	TeacherDTO teacherDTO = new TeacherDTO(teacherEntity);
		    	teachers.add(teacherDTO);
		    }
		    	    
		    for(Link linkEntity : studentEntity.getLinks()){
		    	LinkDTO linkDTO = new LinkDTO(linkEntity);
		    	links.add(linkDTO);
		    }
            		
		}

	}
	
	
	// ==========================================
        // =          BUSINESS METHODS: fromDTO     =
	// ==========================================

	/*Convert DTO to Entity via Setters and Persist Entity to Database*/
	public Student fromDTO(EntityManager entityManager) {
		
		Student studentEntity = new Student();
		
		studentEntity.setUUid(this.UUid);
		studentEntity.setFirstName(this.firstName);
		studentEntity.setLastName(this.lastName);
		studentEntity.setEmail(this.email);
		studentEntity.setUsername(this.username);
		studentEntity.setPassword(this.password);
		studentEntity.setFavouriteSubject(this.favouriteSubject);
		
		entityManager.persist(studentEntity);
		
		return studentEntity;
	}
	
		
	// ===========================================
        // =             GETTERS & SETTERS           =
        // ===========================================
    
	
	public Long getId() {
		return id;
	}

	public String getUUid() {
		return UUid;
	}

	public void setUUid(String uUid) {
		UUid = uUid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFavouriteSubject() {
		return favouriteSubject;
	}

	public void setFavouriteSubject(String favouriteSubject) {
		this.favouriteSubject = favouriteSubject;
	}

	public Set<CourseDTO> getCourses() {
		return courses;
	}

	public void setCourses(Set<CourseDTO> courses) {
		this.courses = courses;
	}
	
	@XmlTransient
	public Set<TeacherDTO> getTeachers() {
		return teachers;
	}

	public void setTeachers(Set<TeacherDTO> teachers) {
		this.teachers = teachers;
	}
	
	
	// ==========================================
        // =          METHODS:  toString            =
        // ==========================================
	

	@Override
	public String toString() {
		return "StudentDTO [First Name: " + firstName + ", Last Name: " + lastName 
		+ ", Username: " + username + ", Email: " + email
		+ ", Favourite Subject: " + favouriteSubject + "]";
	}
		
}



