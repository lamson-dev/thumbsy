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
	// /**
	// * get All the messages in the list
	// *
	// * @param kind
	// * : message kind
	// * @return all the messages
	// */
	// public static Iterable<Entity> getAllMessages() {
	// Iterable<Entity> entities = DatastoreUtils.listEntities("Item", null,
	// null);
	// return entities;
	// }
	//
	// /**
	// * Get the message by name, return an Iterable
	// *
	// * @param messageId
	// * : message name
	// * @return Item Entity
	// */
	// public static Iterable<Entity> getMessage(Long messageId) {
	// Iterable<Entity> entities = DatastoreUtils.listEntities("Item", "name",
	// messageId);
	// return entities;
	// }
	//
	// /**
	// * Get all the messages for a conversation
	// *
	// * @param kind
	// * : message kind
	// * @param conversationId
	// * : conversation name
	// * @return: all messages of type conversation
	// */
	// public static Iterable<Entity> getMessagesForConversation(String kind,
	// Long conversationId) {
	// Key ancestorKey = KeyFactory.createKey("Conversation", conversationId);
	// return DatastoreUtils.listChildren("Message", ancestorKey);
	// }
	//
	// /**
	// * get Item with message name
	// *
	// * @param messageName
	// * : get messageName
	// * @return message entity
	// */
	// public static Entity getSingleMessage(Long id) {
	// Query query = new Query(Message.ENTITY_NAME);
	// query.setFilter(new Query.FilterPredicate(Message.PROPERTY_ID,
	// Query.FilterOperator.EQUAL, id));
	// // query.addFilter(Message.PROPERTY_ID, Query.FilterOperator.EQUAL, id);
	// List<Entity> results = DatastoreUtils.getDatastoreServiceInstance()
	// .prepare(query).asList(FetchOptions.Builder.withDefaults());
	// if (!results.isEmpty()) {
	// return (Entity) results.remove(0);
	// }
	// return null;
	// }
	//
	// public static String deleteMessage(Long id) {
	// Entity entity = getSingleMessage(id);
	// if (entity != null) {
	// DatastoreUtils.deleteEntity(entity.getKey());
	// return ("Item deleted successfully.");
	// } else
	// return ("Item not found");
	// }
}
