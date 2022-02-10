package it.socialnetwork.project.model;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;




@Entity
public class UserMessage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Type(type="org.hibernate.type.UUIDCharType")
	private UUID uuid;
	private String recipient;
	private String body;
	private String sender;
	private Timestamp timestamp; //quando è ricevuto dal server
	private boolean hasReceived = false; // indica se il destinatario ha ricevuto il messaggio
	private Long chatId;
	
	
	
	public UserMessage() {}
	
	public UserMessage(UUID uuid, String recipient, String sender, String body, Timestamp timestamp, Long chatId) {
		this.uuid = uuid;
		this.recipient = recipient;
		this.sender = sender;
		this.body = body;
		this.timestamp = timestamp;
		this.chatId = chatId;
	}
	
	public UserMessage(Long id, UUID uuid, String recipient, String sender, String body, Timestamp timestamp, Long chatId) {
		this.id = id;
		this.uuid = uuid;
		this.recipient = recipient;
		this.sender = sender;
		this.body = body;
		this.timestamp = timestamp;
		this.chatId = chatId;
	}
	
	// TODO sistemare
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_users")
	private Chat chat;
	
	 public boolean isHasReceived() {
		return hasReceived;
	}

	public void setHasReceived(boolean hasReceived) {
		this.hasReceived = hasReceived;
	}

	
	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	
	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
	        return id;
	    }
	
	public UUID getUUID() {
		return uuid;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}


	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	
	
	
}
