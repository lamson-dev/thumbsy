package me.lamson.thumbsy.appengine;

import static me.lamson.thumbsy.appengine.OfyService.ofy;

import java.util.List;

import me.lamson.thumbsy.models.Conversation;
import me.lamson.thumbsy.models.Message;

import com.googlecode.objectify.Key;

public class MessageDao {

	private final static int DEFAULT_LIMIT_MESSAGES = 50;

	public static void createMessage(Message msg) {
		msg.setConversationKey(Key.create(Conversation.class,
				msg.getConversationId()));
		ofy().save().entity(msg);
	}

	public static void deleteMessage(Message msg) {
		ofy().delete().entity(msg);
	}

	public static void deleteMessageById(Long id) {
		ofy().delete().type(Message.class).id(id);
	}

	public static Message getMessageById(Long id) {
		return ofy().load().type(Message.class).id(id).get();
	}

	public static List<Message> getMessagesByConversationId(Long id) {
		return ofy().load().type(Message.class)
				.filter(Message.PROPERTY_CONVERSATION_ID, id)
				.limit(DEFAULT_LIMIT_MESSAGES).list();
	}
}
