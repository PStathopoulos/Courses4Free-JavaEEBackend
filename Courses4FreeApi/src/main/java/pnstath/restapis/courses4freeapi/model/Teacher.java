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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "Teachers")
@NamedQueries({ @NamedQuery(name = Teacher.FIND_ALL, query = "SELECT t FROM Teacher t ORDER BY t.lastName"),
				@NamedQuery(name = Teacher.FIND_BY_CREDENTIALS, 
							query = "SELECT t FROM Teacher t WHERE t.username = ?1 AND t.password = ?2"),
				@NamedQuery(name = Teacher.COUNT_ALL, query = "SELECT COUNT(t) FROM Teacher t")
})
@XmlRootElement
	public class Teacher extends User implements Serializable {
	
	
	// ==========================================
    // =                CONSTANTS               =
    // ==========================================
	
	
	private static final long serialVersionUID = 1L;	
	public static final String FIND_ALL = "Teacher.findAll";
	public static final String FIND_BY_CREDENTIALS = "Teacher.findByCredentials";
	public static final String COUNT_ALL = "Teacher.countAll";
	
	
	// ==========================================
    // =                ATTRIBUTES              =
    // ==========================================
	
	
	@Column(name = "Years_Experience")
	@NotNull(message = "The experience of a teacher in years must be at least 0")
	@Min(0)
	private Double yearsExperience;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="teacher", orphanRemoval = true)
	private Set<Course> courses = new HashSet<Course>();
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Student> students = new HashSet<Student>();
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="Link_FK")
	private Set <Link> links = new HashSet<Link>();
	
	
	// ==========================================
    // =                CONSTRUCTORS            =
    // ==========================================
	
	
	public Teacher(){	
	}
	
	public Teacher(String firstName, String lastName, String email, String username, String password,
			Double yearsExperience) {
		super(firstName, lastName, email, username, password);
		this.yearsExperience = yearsExperience;
	}

	
	// ===========================================
    // =             GETTERS & SETTERS           =
    // ===========================================
	

	public Double getYearsExperience() {
		return yearsExperience;
	}

	public void setYearsExperience(Double yearsExperience) {
		this.yearsExperience = yearsExperience;
	}
	
	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	
	public Set<Link> getLinks() {
		return links;
	}

	public void setLinks(Set<Link> links) {
		this.links = links;
	}
	
	
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
		return "Teacher [First Name: " + getFirstName() + ", Last Name: " + getLastName()  
		+ ", Username: " + getUsername() + ", Email: " + getEmail() 
		+ ", Years Experience: " + yearsExperience + "]";
	}
	
}
