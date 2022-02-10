package it.socialnetwork.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String alias; 	 
	private String username; 
	private String email;
	private String[] roles; //TODO salvarlo in modo migliore
	private String password;
	
	public User() {}
	
	public User(String alias, String username, String email, String password, String[] roles) {
		super();
		this.alias = alias;
		this.username = username;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}


	public String getAlias() {
		return alias;
	}


	public void setAlias(String alias) {
		this.alias = alias;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getRoles() {
		return roles;
	}


	public void setRoles(String[] roles) {
		this.roles = roles;
	}
	
	
}
