package me.lamson.thumbsy.models;

import javax.xml.bind.annotation.XmlRootElement;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@XmlRootElement
@Entity
public class Conversation {

	public static final String ENTITY_NAME = "Conversation";
	public static final String PROPERTY_ID = "conversationId";
	public static final String PROPERTY_USER_ID = "content";
	public static final String PROPERTY_CONTENT = "content";

	@Id
	private Long id;
	private String userId;
	private String recipientName;
	private Integer numberOfMessages = 0;

	public Conversation() {
	}

	public Conversation(Long id) {
		this.id = id;
	}

	public Conversation(Long id, String userId, String content) {
		this.id = id;
		this.userId = userId;
		this.recipientName = content;
	}

	public String getUserId() {
		return userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return recipientName;
	}

	public void setContent(String content) {
		this.recipientName = content;
	}

	public Integer getNumberOfMessages() {
		return numberOfMessages;
	}

	public void updateNumberOfMessages() {
		this.numberOfMessages++;
	}
}