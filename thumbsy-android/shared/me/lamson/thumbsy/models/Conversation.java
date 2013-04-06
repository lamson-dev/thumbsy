package me.lamson.thumbsy.models;

public class Conversation {

	public static final String ENTITY_NAME = "conversation";
	public static final String PROPERTY_ID = "conversationId";
	public static final String PROPERTY_CONTENT = "content";

	private Long id;
	private String content;

	public Conversation() {
	}

	public Conversation(Long id) {
		this.id = id;
	}

	public Conversation(Long id, String content) {
		this.id = id;
		this.content = content;
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
