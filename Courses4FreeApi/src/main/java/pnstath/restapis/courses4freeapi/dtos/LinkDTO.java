package pnstath.restapis.courses4freeapi.dtos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import pnstath.restapis.courses4freeapi.model.Link;

@XmlRootElement
public class LinkDTO implements Serializable{
	

	// ==========================================
        // =                CONSTANTS               =
        // ==========================================
	
	private static final long serialVersionUID = 1L;
	
	
	// ==========================================
        // =                ATTRIBUTES              =
        // ==========================================
	
	
	private Long id;
	private String href;
	private String rel;
	
	
	// ==========================================
        // =                CONSTRUCTORS            =
        // ==========================================
	
	
	public LinkDTO(){
		
	}
	
	// Convert Entity to DTO via Constructor
	public LinkDTO(Link linkEntity) {
		if (linkEntity != null) {
			this.id = linkEntity.getId();
			this.href = linkEntity.getHref();
			this.rel = linkEntity.getRel();
		}
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
	public void setLink(String href) {
		this.href = href;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	
	// ==========================================
        // =          METHODS:  toString            =
        // ==========================================

	@Override
	public String toString() {
		return "LinkDTO [href=" + href + ", rel=" + rel + "]";
	}
			
}



