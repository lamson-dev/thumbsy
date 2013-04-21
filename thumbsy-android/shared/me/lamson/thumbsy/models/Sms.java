package me.lamson.thumbsy.models;

import javax.xml.bind.annotation.XmlRootElement;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@XmlRootElement
@Entity
// @XmlType(propOrder = { "id", "incoming", "content" })
public class Sms {

	public static final String ENTITY_NAME = "Message";
	public static final String PROPERTY_ID = "messageId";
	public static final String PROPERTY_THREAD_ID = "threadId";
	public static final String PROPERTY_THREAD_KEY = "threadKey";
	public static final String PROPERTY_ADDRESS = "address";
	public static final String PROPERTY_READ = "read";
	public static final String PROPERTY_INCOMING = "incoming";
	public static final String PROPERTY_BODY = "body";
	public static final String PROPERTY_DATE = "date";

	@Id
	private Long id;
	@Index
	transient private Key<SmsThread> threadKey;
	@Index
	private Long threadId;
	@Index
	private String address;
	@Index
	private Boolean read;
	private Boolean incoming;
	private String body;
	@Index
	private Long date;
	@Ignore
	private String userId;

	public Sms() {
	}

	public Sms(Long id) {
		this.setId(id);
	}

	public Sms(Long msgId, String msgBody, String msgAddress, Boolean incoming,
			Long date) {
		this.setId(msgId);
		this.setBody(msgBody);
		this.setAddress(msgAddress);
		this.setIncoming(incoming);
		this.setDate(date);
	}

	public Sms(Long id, Long threadId, String address, Boolean read,
			Boolean incoming, String body) {
		this.setId(id);
		this.setThreadId(threadId);
		this.setAddress(address);
		this.setRead(read);
		this.setIncoming(incoming);
		this.setBody(body);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getThreadId() {
		return threadId;
	}

	public void setThreadId(Long threadId) {
		this.threadId = threadId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean isRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public Boolean isIncoming() {
		return incoming;
	}

	public void setIncoming(Boolean incoming) {
		this.incoming = incoming;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Key<SmsThread> getThreadKey() {
		return threadKey;
	}

	public void setThreadKey(Key<SmsThread> threadKey) {
		this.threadKey = threadKey;
	}

}