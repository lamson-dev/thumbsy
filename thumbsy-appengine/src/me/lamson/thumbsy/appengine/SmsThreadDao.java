package me.lamson.thumbsy.appengine;

import static me.lamson.thumbsy.appengine.OfyService.ofy;

import java.util.List;

import me.lamson.thumbsy.models.SmsThread;

/**
 * This class handles all the CRUD operations related to Product entity.
 * 
 */
public class SmsThreadDao {

	private static Long idGenerator = Long.valueOf(0);

	public static Long storeThread(SmsThread thread) {
		idGenerator++;
		thread.setId(idGenerator);
		ofy().save().entity(thread);
		return idGenerator - 1;
		// return null;
	}

	public static void deleteConversation(SmsThread thread) {
		ofy().delete().entity(thread);
	}

	public static void deleteConversationById(Long id) {
		ofy().delete().type(SmsThread.class).id(id);
	}

	public static SmsThread getThreadById(Long id) {
		return ofy().load().type(SmsThread.class).id(id).get();
	}

	public static List<SmsThread> getThreadsByUserId(String userId) {
		return ofy().load().type(SmsThread.class)
				.filter(SmsThread.PROPERTY_USER_ID, userId).list();
	}

	public static List<SmsThread> getAllThreads() {
		return ofy().load().type(SmsThread.class).list();
	}

	public static void createAndStoreThread(Long id, String userId,
			String address) {

		SmsThread thread = new SmsThread(id, userId, address);
		storeThread(thread);
	}

	public static List<SmsThread> getSmssByAddress(String userId, String address) {

		return null;
	}

}
