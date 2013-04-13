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

}
