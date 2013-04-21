package me.lamson.thumbsy.appengine.dao;

import static me.lamson.thumbsy.appengine.OfyService.ofy;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;

import me.lamson.thumbsy.models.SmsThread;

/**
 * This class handles all the CRUD operations related to Product entity.
 * 
 */
public class SmsThreadDao {

	// private static Long idGenerator = Long.valueOf(0);

	public static Long storeThread(SmsThread thread) {
		// idGenerator++;
		// thread.setId(null);
		ofy().save().entity(thread);
		// return idGenerator - 1;
		return null;
	}

	public static void deleteConversation(SmsThread thread) {
		ofy().delete().entity(thread);
	}

	public static void deleteConversationById(String id) {
		ofy().delete().type(SmsThread.class).id(id);
	}

	public static SmsThread getThreadById(String id) {
		return ofy().load().type(SmsThread.class).id(id).get();
		// Key<SmsThread> key = Key.create(SmsThread.class, id);
		// return ofy().load().key(key).get();
	}

	public static List<SmsThread> getThreadsByUserId(String userId) {
		// return ofy().load().type(SmsThread.class)
		// .filter(SmsThread.PROPERTY_USER_ID, userId)
		// .order(SmsThread.PROPERTY_DATE).list();
		return ofy().load().type(SmsThread.class)
				.filter(SmsThread.PROPERTY_USER_ID, userId)
				.order("-" + SmsThread.PROPERTY_DATE).list();
	}

	public static List<SmsThread> getAllThreads() {
		return ofy().load().type(SmsThread.class)
				.order("-" + SmsThread.PROPERTY_DATE).list();
	}

	public static void createAndStoreThread(String id, String userId,
			String address) {
		SmsThread thread = new SmsThread(id, userId, address);
		thread.setDate(new Date().getTime());
		storeThread(thread);
	}

}
