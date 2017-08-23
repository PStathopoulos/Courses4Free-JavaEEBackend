package pnstath.restapis.courses4freeapi.dtos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlReadOnly;
import org.hibernate.validator.constraints.NotBlank;

import pnstath.restapis.courses4freeapi.model.Course;
import pnstath.restapis.courses4freeapi.model.Link;
import pnstath.restapis.courses4freeapi.model.Student;
import pnstath.restapis.courses4freeapi.model.Teacher;


@XmlRootElement
public class TeacherDTO implements Serializable {
	
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
	@NotNull(message = "The experience of a teacher in years must be at least 0")
	@Min(0)
	private Double yearsExperience;
	
	
	private Set<CourseDTO> courses = new HashSet<CourseDTO>();
        private Set<StudentDTO> students = new HashSet<StudentDTO>();
	private Set <LinkDTO> links = new HashSet<LinkDTO>();
	
	
	// ==========================================
        // =                CONSTRUCTORS            =
        // ==========================================
	
	
	public TeacherDTO(){	
	}
	
	// Convert Entity to DTO via Constructor
	public TeacherDTO(Teacher teacherEntity) {
		if (teacherEntity != null) {
			this.id = teacherEntity.getId();
			this.UUid = teacherEntity.getUUid();
			this.firstName = teacherEntity.getFirstName();
			this.lastName = teacherEntity.getLastName();
			this.email = teacherEntity.getEmail();
			this.username = teacherEntity.getUsername();
			this.password = teacherEntity.getPassword();
			this.yearsExperience = teacherEntity.getYearsExperience();
		    	
		    for(Course courseEntity : teacherEntity.getCourses()){
		    	CourseDTO courseDTO = new CourseDTO(courseEntity);
		    	courses.add(courseDTO);
		    }
		   
		    for(Student studentEntity : teacherEntity.getStudents()){
		    	StudentDTO studentDTO = new StudentDTO(studentEntity);
		    	students.add(studentDTO);
		    }
		    	    
		    for(Link linkEntity : teacherEntity.getLinks()){
		    	LinkDTO linkDTO = new LinkDTO(linkEntity);
		    	links.add(linkDTO);
		    }
            		
		}

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

	public Double getYearsExperience() {
		return yearsExperience;
	}

	public void setYearsExperience(Double yearsExperience) {
		this.yearsExperience = yearsExperience;
	}
	
	public Set<LinkDTO> getLinks() {
		return links;
	}

	public void setLinks(Set<LinkDTO> links) {
		this.links = links;
	}
	
	public Set<CourseDTO> getCourses() {
		return courses;
	}

	public void setCourses(Set<CourseDTO> courses) {
		this.courses = courses;
	}

	public Set<StudentDTO> getStudents() {
		return students;
	}

	public void setStudents(Set<StudentDTO> students) {
		this.students = students;
	}
	
	// ==========================================
        // =          BUSINESS METHODS: fromDTO     =
	// ==========================================

	/*Convert DTO to Entity via Setters and Persist Entity to Database*/
	public Teacher fromDTO(EntityManager entityManager) {
		
		Teacher teacherEntity = new Teacher();
		
		teacherEntity.setUUid(this.UUid);
		teacherEntity.setFirstName(this.firstName);
		teacherEntity.setLastName(this.lastName);
		teacherEntity.setEmail(this.email);
		teacherEntity.setUsername(this.username);
		teacherEntity.setPassword(this.password);
		teacherEntity.setYearsExperience(this.yearsExperience);
		
		entityManager.persist(teacherEntity);
		
		return teacherEntity;
	}
	
	
	// ==========================================
        // =          METHODS:  toString            =
        // ==========================================
	

	@Override
	public String toString() {
		return "TeacheDTO [First Name: " + firstName + ", Last Name: " + lastName  
		+ ", Username: " + username + ", Email: " + email
		+ ", Years Experience: " + yearsExperience + "]";
	}
	

}
