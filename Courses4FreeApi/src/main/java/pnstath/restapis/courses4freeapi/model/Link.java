package pnstath.restapis.courses4freeapi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;


@Entity(name = "Links")
@XmlRootElement
public class Link implements Serializable{
	
	
        // ==========================================
        // =                CONSTANTS               =
        // ==========================================
	
	
	private static final long serialVersionUID = 1L;
	
	
        // ==========================================
        // =                ATTRIBUTES              =
        // ==========================================
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Generated - Auto Increment
	@Column
	private Long id;
	private String href;
	private String rel;
	
	
        // ==========================================
        // =                CONSTRUCTORS            =
        // ==========================================
	
	public Link(){
		
	}
	
	public Link(String link, String rel) {
		this.href = link;
		this.rel = rel;
	}
	
	
       // ===========================================
       // =             GETTERS & SETTERS           =
       // ===========================================
	
	public Long getId() {
		return id;
	}
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}

	
       // ==========================================
       // =  METHODS: hashCode, equals, toString   =
       // ==========================================
	 
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((href == null) ? 0 : href.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((rel == null) ? 0 : rel.hashCode());
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
		Link other = (Link) obj;
		if (href == null) {
			if (other.href != null)
				return false;
		} else if (!href.equals(other.href))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (rel == null) {
			if (other.rel != null)
				return false;
		} else if (!rel.equals(other.rel))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Link [href=" + href + ", rel=" + rel + "]";
	}
			
}
