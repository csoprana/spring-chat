package it.socialnetwork.project.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String user1;
	private String user2;
	
	// da controllare
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "chat")
    private List<UserMessage> userMessages;
	
	public Chat() {}
	
	public Chat(String user1, String user2) {
		super();
		this.user1 = user1;
		this.user2 = user2;
	}
	public String getUser1() {
		return user1;
	}
	public void setUser1(String user1) {
		this.user1 = user1;
	}
	public String getUser2() {
		return user2;
	}
	public void setUser2(String user2) {
		this.user2 = user2;
	}
	
	
}
