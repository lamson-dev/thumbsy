package me.lamson.thumbsy.appengine;

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

	/**
	 * Update the conversation
	 * 
	 * @param name
	 *            : name of the conversation
	 * @param description
	 *            : description
	 * @return updated conversation
	 */
	public static void createOrUpdateConversation(Conversation conv) {
		Entity conversation = getConversation(conv.getId());
		if (conversation == null) {
			conversation = new Entity(Conversation.ENTITY_NAME, conv.getId());
			conversation.setProperty(Conversation.PROPERTY_CONTENT,
					conv.getContent());
		} else {
			conversation.setProperty(Conversation.PROPERTY_CONTENT,
					conv.getContent());
		}
		Util.persistEntity(conversation);
	}

	/**
	 * Return all the conversations
	 * 
	 * @param kind
	 *            : of kind conversation
	 * @return conversations
	 */
	public static Iterable<Entity> getAllConversations(String kind) {
		return Util.listEntities(kind, null, null);
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
		return Util.findEntity(key);
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
		List<Entity> results = Util.getDatastoreServiceInstance()
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
		Util.deleteEntity(key);
		return "Product deleted successfully";

	}
}
