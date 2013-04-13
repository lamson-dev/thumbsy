package me.lamson.thumbsy.models;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@XmlRootElement
@Entity
@XmlType(propOrder = { "id", "incoming", "content" })
public class Message {

	public static final String ENTITY_NAME = "Message";
	public static final String PROPERTY_ID = "messageId";
	public static final String PROPERTY_INCOMING = "incoming";
	public static final String PROPERTY_CONTENT = "content";
	public static final String PROPERTY_CONVERSATION_ID = "conversationId";

	@Id
	private Long id;
	transient private Key<Conversation> conversationKey;
	@Index
	private Long conversationId;
	private Boolean incoming;
	private String content;

	public Message() {
	}

	public Message(Long id) {
		this.id = id;
	}

	public Message(Long id, Long conversationId,
			Key<Conversation> conversationKey, String content, Boolean incoming) {
		this.id = id;
		this.conversationId = conversationId;
		this.conversationKey = conversationKey;
		this.content = content;
		this.setIncoming(incoming);
	}

	public Message(Long id, Long conversationId, String content,
			Boolean incoming) {
		this.id = id;
		this.conversationId = conversationId;
		this.content = content;
		this.setIncoming(incoming);
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

	@XmlTransient
	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	public Key<Conversation> getConversation() {
		return conversationKey;
	}

	public void setConversationKey(Key<Conversation> conversation) {
		this.conversationKey = conversation;
	}

	public Boolean isIncoming() {
		return incoming;
	}

	public void setIncoming(Boolean incoming) {
		this.incoming = incoming;
	}

}
