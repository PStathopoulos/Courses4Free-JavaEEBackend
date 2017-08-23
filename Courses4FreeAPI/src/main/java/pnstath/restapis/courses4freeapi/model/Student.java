package pnstath.restapis.courses4freeapi.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "Students")
@NamedQueries({ @NamedQuery(name = Student.FIND_ALL, query = "SELECT s FROM Student s ORDER BY s.lastName"),
    			@NamedQuery(name = Student.FIND_BY_CREDENTIALS, 
    			query = "SELECT s FROM Student s WHERE s.username = ?1 AND s.password = ?2"),
    			@NamedQuery(name = Student.COUNT_ALL, query = "SELECT COUNT(s) FROM Student s"),
    			
})
@XmlRootElement
public class Student extends User implements Serializable  {
	
	
	// ==========================================
    // =                CONSTANTS               =
    // ==========================================
	
	
	private static final long serialVersionUID = 1L;	
	public static final String FIND_ALL = "Student.findAll";
	public static final String FIND_BY_CREDENTIALS = "Student.findByCredentials";
	public static final String COUNT_ALL = "Student.countAll";
	
	
	// ==========================================
    // =                ATTRIBUTES              =
    // ==========================================
	
	
	@Column(name = "Favourite_Subject")
	private String favouriteSubject;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "students")
    private Set<Course> courses = new HashSet<Course>();	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Teacher> teachers;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="Link_FK")
	private Set <Link> links = new HashSet<Link>();
	
	
	// ==========================================
    // =                CONSTRUCTORS            =
    // ==========================================
	
	
	public Student(){		
	}
	
	public Student(String firstName, String lastName, String email, String username, String password) {
		super(firstName, lastName, email, username, password);

	}
	
	
	// ===========================================
    // =             GETTERS & SETTERS           =
    // ===========================================
    
	
	public String getFavouriteSubject() {
		return favouriteSubject;
	}

	public void setFavouriteSubject(String favouriteSubject) {
		this.favouriteSubject = favouriteSubject;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}
	
	@XmlTransient
	public Set<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(Set<Teacher> teachers) {
		this.teachers = teachers;
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
		result = prime * result + ((getFirstName() == null) ? 0 : getFirstName().hashCode());
		result = prime * result + (( getLastName()  == null) ? 0 :  getLastName() .hashCode());
		result = prime * result + (( getEmail()  == null) ? 0 :  getEmail() .hashCode());
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
		Student other = (Student) obj;
		if (getFirstName() == null) {
			if (other.getFirstName() != null)
				return false;
		} else if (!getFirstName().equals(other.getFirstName()))
			return false;
		if (getLastName() == null) {
			if (other.getLastName()  != null)
				return false;
		} else if (!getLastName() .equals(other.getLastName() ))
			return false;
		if (getEmail() == null) {
			if (other.getEmail()  != null)
				return false;
		} else if (!getEmail() .equals(other.getEmail() ))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Student [First Name: " + getFirstName() + ", Last Name: " + getLastName()  
		+ ", Username: " + getUsername() + ", Email: " + getEmail() 
		+ ", Favourite Subject: " + favouriteSubject + "]";
	}
		
}
