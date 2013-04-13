package me.lamson.thumbsy.appengine;

import static me.lamson.thumbsy.appengine.OfyService.ofy;

import java.util.List;

import me.lamson.thumbsy.models.Conversation;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

/**
 * This class handles all the CRUD operations related to Product entity.
 * 
 */
public class ConversationDao {

	public static void createConversation(Conversation conversation) {
		ofy().save().entity(conversation);
	}

	public static void deleteConversation(Conversation conv) {
		ofy().delete().entity(conv);
	}

	public static void deleteConversationById(Long id) {
		ofy().delete().type(Conversation.class).id(id);
	}

	public static Conversation getConversationById(Long id) {
		return ofy().load().type(Conversation.class).id(id).get();
	}

	public static List<Conversation> getAllConversations() {
		return ofy().load().type(Conversation.class).list();
	}

	/**
	 * Get conversation entity
	 * 
	 * @param id
	 *            : name of the conversation
	 * @return: conversation entity
	 */
	public static Entity getConversation(Long id) {
		Key key = KeyFactory.createKey(Conversation.ENTITY_NAME, id);
		return DatastoreUtils.findEntity(key);
	}

	/**
	 * Get all messages for a conversation
	 * 
	 * @param conversationId
	 *            : name of the conversation
	 * @return list of messages
	 */

	public static List<Entity> getMessages(Long conversationId) {
		Query query = new Query();
		Key parentKey = KeyFactory.createKey(Conversation.ENTITY_NAME,
				conversationId);
		query.setAncestor(parentKey);
		query.setFilter(new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY,
				Query.FilterOperator.GREATER_THAN, parentKey));
		// query.addFilter(Entity.KEY_RESERVED_PROPERTY,
		// Query.FilterOperator.GREATER_THAN, parentKey);
		List<Entity> results = DatastoreUtils.getDatastoreServiceInstance()
				.prepare(query).asList(FetchOptions.Builder.withDefaults());
		return results;
	}

	/**
	 * Delete conversation entity
	 * 
	 * @param conversationId
	 *            : conversation to be deleted
	 * @return status string
	 */
	public static String deleteConversation(Long conversationId) {
		Key key = KeyFactory.createKey("Product", conversationId);

		List<Entity> messages = getMessages(conversationId);
		if (!messages.isEmpty()) {
			return "Cannot delete, as there are messages associated with this conversation.";
		}
		DatastoreUtils.deleteEntity(key);
		return "Product deleted successfully";

	}
}
