package me.lamson.thumbsy.appengine.dao;
//package me.lamson.thumbsy.appengine;
//
//import static me.lamson.thumbsy.appengine.OfyService.ofy;
//
//import java.util.List;
//
//import me.lamson.thumbsy.models.Message;
//
//import com.google.appengine.api.datastore.Entity;
//import com.google.appengine.api.datastore.FetchOptions;
//import com.google.appengine.api.datastore.Key;
//import com.google.appengine.api.datastore.KeyFactory;
//import com.google.appengine.api.datastore.Query;
//
///**
// * This class handles CRUD operations related to Item entity.
// * 
// * 
// */
//
//public class MessageDaoCopy {
//
//	public static void createMessage(Message msg) {
//		ofy().save().entity(msg);
//	}
//
//	public static void deleteMessage(Message msg) {
//
//	}
//
//	/**
//	 * Create or update Item for a particular conversation. Product entity has
//	 * one to many relation-ship with Item entity
//	 * 
//	 * @param conversationId
//	 *            : conversation name for which the message is created.
//	 * @param messageName
//	 *            : message name
//	 * @param price
//	 *            : price of the message
//	 * @return
//	 */
//	public static Entity createOrUpdateMessage(Message msg) {
//		Entity conversation = ConversationDaoCopy.getConversation(msg
//				.getConversationId());
//		Entity message = new Entity(Message.ENTITY_NAME, conversation.getKey());
//		message.setProperty(Message.PROPERTY_ID, msg.getId());
//		message.setProperty(Message.PROPERTY_CONVERSATION_ID,
//				msg.getConversationId());
//		message.setProperty(Message.PROPERTY_CONTENT, msg.getContent());
//		message.setProperty(Message.PROPERTY_INCOMING, msg.isIncoming());
//		DatastoreUtils.persistEntity(message);
//		return message;
//	}
//
//	public static Entity insertMessage(Message msg) {
//		Entity conversation = ConversationDaoCopy.getConversation(msg
//				.getConversationId());
//		Entity message = getSingleMessage(msg.getId());
//		if (message == null) {
//			// create new message entity
//			message = new Entity(Message.ENTITY_NAME, conversation.getKey());
//			message.setProperty(Message.PROPERTY_ID, msg.getId());
//			message.setProperty(Message.PROPERTY_CONVERSATION_ID,
//					msg.getConversationId());
//			message.setProperty(Message.PROPERTY_CONTENT, msg.getContent());
//			message.setProperty(Message.PROPERTY_INCOMING, msg.isIncoming());
//		} else {
//
//			// update message entity
//			// if (price != null && !"".equals(price)) {
//			// message.setProperty("price", price);
//			// }
//		}
//		DatastoreUtils.persistEntity(message);
//		return message;
//	}
//
//	/**
//	 * get All the messages in the list
//	 * 
//	 * @param kind
//	 *            : message kind
//	 * @return all the messages
//	 */
//	public static Iterable<Entity> getAllMessages() {
//		Iterable<Entity> entities = DatastoreUtils.listEntities("Item", null,
//				null);
//		return entities;
//	}
//
//	/**
//	 * Get the message by name, return an Iterable
//	 * 
//	 * @param messageId
//	 *            : message name
//	 * @return Item Entity
//	 */
//	public static Iterable<Entity> getMessage(Long messageId) {
//		Iterable<Entity> entities = DatastoreUtils.listEntities("Item", "name",
//				messageId);
//		return entities;
//	}
//
//	/**
//	 * Get all the messages for a conversation
//	 * 
//	 * @param kind
//	 *            : message kind
//	 * @param conversationId
//	 *            : conversation name
//	 * @return: all messages of type conversation
//	 */
//	public static Iterable<Entity> getMessagesForConversation(String kind,
//			Long conversationId) {
//		Key ancestorKey = KeyFactory.createKey("Conversation", conversationId);
//		return DatastoreUtils.listChildren("Message", ancestorKey);
//	}
//
//	/**
//	 * get Item with message name
//	 * 
//	 * @param messageName
//	 *            : get messageName
//	 * @return message entity
//	 */
//	public static Entity getSingleMessage(Long id) {
//		Query query = new Query(Message.ENTITY_NAME);
//		query.setFilter(new Query.FilterPredicate(Message.PROPERTY_ID,
//				Query.FilterOperator.EQUAL, id));
//		// query.addFilter(Message.PROPERTY_ID, Query.FilterOperator.EQUAL, id);
//		List<Entity> results = DatastoreUtils.getDatastoreServiceInstance()
//				.prepare(query).asList(FetchOptions.Builder.withDefaults());
//		if (!results.isEmpty()) {
//			return (Entity) results.remove(0);
//		}
//		return null;
//	}
//
//	public static String deleteMessage(Long id) {
//		Entity entity = getSingleMessage(id);
//		if (entity != null) {
//			DatastoreUtils.deleteEntity(entity.getKey());
//			return ("Item deleted successfully.");
//		} else
//			return ("Item not found");
//	}
//}
