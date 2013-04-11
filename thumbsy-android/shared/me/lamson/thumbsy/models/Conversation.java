package me.lamson.thumbsy.models;

public class Conversation {

	public static final String ENTITY_NAME = "Conversation";
	public static final String PROPERTY_ID = "conversationId";
	public static final String PROPERTY_USER_ID = "content";
	public static final String PROPERTY_CONTENT = "content";

	private Long id;
	private String content;
	private String userId;

	public Conversation() {
	}

	public Conversation(Long id) {
		this.id = id;
	}

	public Conversation(Long id, String userId, String content) {
		this.id = id;
		this.userId = userId;
		this.content = content;
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
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}