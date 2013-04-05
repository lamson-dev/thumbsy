package me.lamson.thumbsy.models;

import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/**
 * This class handles CRUD operations related to Item entity.
 * 
 * 
 */

public class MessageDao {

	/**
	 * Create or update Item for a particular product. Product entity has one to
	 * many relation-ship with Item entity
	 * 
	 * @param conversationId
	 *            : product name for which the item is created.
	 * @param itemName
	 *            : item name
	 * @param price
	 *            : price of the item
	 * @return
	 */
	public static Entity createOrUpdateMessage(Message msg) {
		Entity conversation = ConversationDao.getConversation(msg
				.getConversationId());
		Entity message = getSingleMessage(msg.getId());
		if (message == null) {
			// create new message entity
			message = new Entity(Message.ENTITY_NAME, conversation.getKey());
			message.setProperty(Message.PROPERTY_ID, msg.getId());
			message.setProperty(Message.PROPERTY_CONVERSATION_ID,
					msg.getConversationId());
			message.setProperty(Message.PROPERTY_CONTENT, msg.getContent());
		} else {

			// update message entity
			// if (price != null && !"".equals(price)) {
			// message.setProperty("price", price);
			// }
		}
		Util.persistEntity(message);
		return message;
	}

	/**
	 * get All the items in the list
	 * 
	 * @param kind
	 *            : item kind
	 * @return all the items
	 */
	public static Iterable<Entity> getAllMessages() {
		Iterable<Entity> entities = Util.listEntities("Item", null, null);
		return entities;
	}

	/**
	 * Get the item by name, return an Iterable
	 * 
	 * @param itemName
	 *            : item name
	 * @return Item Entity
	 */
	public static Iterable<Entity> getMessage(String itemName) {
		Iterable<Entity> entities = Util.listEntities("Item", "name", itemName);
		return entities;
	}

	/**
	 * Get all the items for a product
	 * 
	 * @param kind
	 *            : item kind
	 * @param productName
	 *            : product name
	 * @return: all items of type product
	 */
	public static Iterable<Entity> getMessagesForConversation(String kind,
			String productName) {
		Key ancestorKey = KeyFactory.createKey("Product", productName);
		return Util.listChildren("Item", ancestorKey);
	}

	/**
	 * get Item with item name
	 * 
	 * @param itemName
	 *            : get itemName
	 * @return item entity
	 */
	public static Entity getSingleMessage(Long id) {
		Query query = new Query(Message.ENTITY_NAME);
		query.setFilter(new Query.FilterPredicate(Message.PROPERTY_ID,
				Query.FilterOperator.EQUAL, id));
		// query.addFilter(Message.PROPERTY_ID, Query.FilterOperator.EQUAL, id);
		List<Entity> results = Util.getDatastoreServiceInstance()
				.prepare(query).asList(FetchOptions.Builder.withDefaults());
		if (!results.isEmpty()) {
			return (Entity) results.remove(0);
		}
		return null;
	}

	public static String deleteMessage(Long id) {
		Entity entity = getSingleMessage(id);
		if (entity != null) {
			Util.deleteEntity(entity.getKey());
			return ("Item deleted successfully.");
		} else
			return ("Item not found");
	}
}
