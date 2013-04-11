package me.lamson.thumbsy.models;

public class Message {

	public static final String ENTITY_NAME = "Message";
	public static final String PROPERTY_ID = "messageId";
	public static final String PROPERTY_INCOMING = "incoming";
	public static final String PROPERTY_CONTENT = "content";
	public static final String PROPERTY_CONVERSATION_ID = "conversationId";

	private Long id;
	private Long conversationId;
	private Boolean incoming;
	private String content;

	public Message() {
	}

	public Message(Long id) {
		this.id = id;
	}

	public Message(Long id, Long conversationId, String content,
			Boolean incoming) {
		this.id = id;
		this.conversationId = conversationId;
		this.content = content;
		this.incoming = incoming;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean isIncoming() {
		return incoming;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

}
