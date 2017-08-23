package pnstath.restapis.courses4freeapi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.NotBlank;


@Entity
@Table(name = "Courses")
@NamedQueries({ @NamedQuery(name = Course.FIND_ALL, query = "SELECT c FROM Course c ORDER BY c.id DESC"),
	            @NamedQuery(name = Course.FIND_BY_TITLE, query = "SELECT c FROM Course c WHERE c.title = ?1"),
	            @NamedQuery(name = Course.FIND_BY_CATEGORY, query = "SELECT c FROM Course c WHERE c.category = ?1"),
	            @NamedQuery(name = Course.FIND_BY_SUBJECT, query = "SELECT c FROM Course c WHERE c.subject = ?1"),
	            @NamedQuery(name = Course.FIND_BY_TERM, query = "SELECT c FROM Course c WHERE c.title LIKE :term"),
	            @NamedQuery(name = Course.COUNT_ALL, query = "SELECT COUNT(c) FROM Course c")
})

@XmlRootElement
public class Course implements Serializable {

	
       // ==========================================
       // =                CONSTANTS               =
       // ==========================================
	
	
	private static final long serialVersionUID = 1L;
	public static final String FIND_ALL = "Course.findAll";
	public static final String FIND_BY_CATEGORY = "Course.findByCategory";
	public static final String FIND_BY_SUBJECT = "Course.FindByCategory";
	public static final String FIND_BY_TITLE = "Course.findByTitle";
	public static final String FIND_BY_TERM = "Course.findByTerm";
	public static final String COUNT_ALL = "Course.CountAll";
		
	
       // ==========================================
       // =                ATTRIBUTES              =
       // ==========================================
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Generated - Auto Increment
	@Column(name = "ID")
	private Long id;
	@Column(name = "UUID", unique = true, nullable = false)
	private String UUid;
	@Column(name = "Title", unique = true, nullable = false)
	@NotBlank(message = "The title must not be blank")
	private String title;
	@Column(name = "Description", unique = true, nullable = false)
	@NotBlank(message = "The description must not be blank")
	private String description;
	@Column(name = "Subject", nullable = false)
	@NotBlank(message = "The subject must not be blank")
	private String subject;
	@Column(name = "Category", nullable = false)
	@NotBlank(message = "The category must not be blank")
	private String category;
	@Column(name = "Date_Created", nullable = false)
	private Date dateCreated;
	@Column(name = "Photo_Url", unique = true)
	private String photoUrl;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Student> students = new HashSet<Student>();	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="Teacher_FK")
	private Teacher teacher;	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="Link_FK")
	private Set <Link> links = new HashSet<Link>();
	
	
       // ==========================================
       // =               CONSTRUCTORS             =
       // ==========================================
	
	
	public Course() {
	}

	public Course(String title, String description, String subject, String category, String photoUrl) {
		this.title = title;
		this.description = description;
		this.subject = subject;
		this.category = category;
		this.photoUrl = photoUrl;
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
	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	
	@XmlTransient
	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	public Set<Link> getLinks() {
		return links;
	}

	public void setLinks(Set<Link> links) {
		this.links = links;
	}
	
	
       // ==========================================
       // =  METHODS: hashCode, equals, toString   =
       // ==========================================
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Course [UUID:" + UUid + ", Title:" + title + ", Subject:" + subject
				+ ", Category:" + category + "]";
	}
	
}
