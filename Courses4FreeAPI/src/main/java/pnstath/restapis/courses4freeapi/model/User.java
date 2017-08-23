package pnstath.restapis.courses4freeapi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlReadOnly;
import org.hibernate.validator.constraints.NotBlank;


@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
    public abstract class User implements Serializable {
	
	
	// ==========================================
    // =                CONSTANTS               =
    // ==========================================
	
	
	private static final long serialVersionUID = 1L;
	
	
	// ==========================================
    // =                ATTRIBUTES              =
    // ==========================================
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Generated - Auto Increment
	@Column(name = "User_ID")
	private Long id;
	@Column(name = "User_UUID")
	private String UUid;
	@Column(name = "First_Name", nullable = false)
	@NotBlank(message = "The First Name must not be blank")
	private String firstName;
	@Column(name = "Last_Name", nullable = false)
	@NotBlank(message = "The Last Name must not be blank")
	private String lastName;
	@Column(name = "Email", unique = true, nullable = false)
	@Pattern(regexp="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "This is not a valid email adress")
    private String email;
    @Column(name = "Username", unique = true, nullable = false)
    @Size (min = 3, max = 14, message = "Username must contain at least 3 number and at most 14 number of characters")
	private String username;
    @XmlElement
    @XmlReadOnly
    @Column(name = "Password", unique = true,  nullable = false)
    @Pattern(regexp="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})", 
    message = "Password must have at least 6 characters and at most 20, must contain lowercase and uppercase letters, "
    		+ "digits from 0 to 9, one special symbols in the list '@#$%' ")
	private String password;
    
    
    // ==========================================
    // =                CONSTRUCTORS            =
    // ==========================================
   
    
	public User(){		
	}
	
	public User(String firstName, String lastName, String email, String username, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.password = password;
	}
	
	
	// ==========================================
    // =             GETTERS & SETTERS          =
    // ==========================================
	

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
				
}
