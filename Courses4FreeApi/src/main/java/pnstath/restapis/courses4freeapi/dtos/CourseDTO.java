package pnstath.restapis.courses4freeapi.dtos;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.NotBlank;

import pnstath.restapis.courses4freeapi.model.Course;
import pnstath.restapis.courses4freeapi.model.Student;
import pnstath.restapis.courses4freeapi.model.Link;


public class CourseDTO implements Serializable {
	
	
      // ==========================================
      // =                CONSTANTS               =
      // ==========================================
	
	
	private static final long serialVersionUID = 1L;
	
	
       // ==========================================
       // =                ATTRIBUTES              =
       // ==========================================
	
	
	private Long id;
	private String UUid;
	@NotBlank(message = "The title must not be blank")
	private String title;
	@NotBlank(message = "The description must not be blank")
	private String description;
	@NotBlank(message = "The subject must not be blank")
	private String subject;
	@NotBlank(message = "The category must not be blank")
	private String category;
	private Date dateCreated;
	private String photoUrl;
	private Set<StudentDTO> students = new HashSet<StudentDTO>();	
	private TeacherDTO teacher;	
	private Set<LinkDTO> links = new HashSet<LinkDTO>();
	
	
	// ==========================================
	// =               CONSTRUCTORS             =
	// ==========================================

	
	public CourseDTO(){
		
	}
	
	// Convert Entity to DTO via Constructor
	public CourseDTO(Course courseEntity) {
		if (courseEntity != null) {
			this.id = courseEntity.getId();
			this.UUid = courseEntity.getUUid();
			this.title = courseEntity.getTitle();
			this.description = courseEntity.getDescription();
			this.subject = courseEntity.getSubject();
			this.category = courseEntity.getCategory();
			this.dateCreated = courseEntity.getDateCreated();
			this.photoUrl = courseEntity.getPhotoUrl();
		    this.teacher = new TeacherDTO(courseEntity.getTeacher());
			
		    for(Student studentEntity : courseEntity.getStudents()){
		    	StudentDTO studentDTO = new StudentDTO(studentEntity);
		    	students.add(studentDTO);
		    }
		    
		    for(Link linkEntity : courseEntity.getLinks()){
		    	LinkDTO linkDTO = new LinkDTO(linkEntity);
		    	links.add(linkDTO);
		    }
            		
		}

	}
	
	
	// ==========================================
        // =          BUSINESS METHODS: fromDTO     =
	// ==========================================

	//Convert DTO to Entity via Setters and Persist Entity to Database
	public Course fromDTO(EntityManager entityManager) {
		
		Course courseEntity = new Course();
		
		courseEntity.setUUid(this.UUid);
		courseEntity.setTitle(this.title);
		courseEntity.setDescription(this.description);
		courseEntity.setSubject(this.subject);
		courseEntity.setCategory(this.category);
		courseEntity.setPhotoUrl(this.photoUrl);
		
		entityManager.persist(courseEntity);
		return courseEntity;
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

	public void setUUid(String UUid) {
		this.UUid = UUid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	
        public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@XmlTransient
	public Set<StudentDTO> getStudents() {
		return students;
	}

	public void setStudents(Set<StudentDTO> students) {
		this.students = students;
	}
	
	@XmlTransient
	public TeacherDTO getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherDTO teacher) {
		this.teacher = teacher;
	}
	
	
	// ==========================================
        // =      METHODS:  toString                =
        // ==========================================
	

	@Override
	public String toString() {
		return "CourseDTO [UUID:" + UUid + ", Title:" + title + ", Subject:" + subject
				+ ", Category:" + category + "]";
	}
	
}
