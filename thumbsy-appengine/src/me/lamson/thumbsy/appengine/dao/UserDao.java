package me.lamson.thumbsy.appengine.dao;

import static me.lamson.thumbsy.appengine.OfyService.ofy;

import java.util.logging.Logger;

import me.lamson.thumbsy.models.User;

public class UserDao {

	protected final static Logger logger = Logger.getLogger("UserDao");

	public static void storeUser(User user) {
		// user.setThreadKey(Key.create(UserThread.class,
		// user.getUserId() + user.getAddress()));
		ofy().save().entity(user);
	}

	public static void deleteUser(User user) {
		ofy().delete().entity(user);
	}

	public static void deleteUserById(long id) {
		ofy().delete().type(User.class).id(id);
	}

	public static User getUserById(long id) {
		return ofy().load().type(User.class).id(id).get();
	}

	public static User getUserByGoogleUserId(String googleUserId) {
		return ofy().load().type(User.class)
				.filter(User.PROPERTY_GOOGLE_ID, googleUserId).list().get(0);
	}

	public static String getCurrentConversationAddress(String userId) {
		User user = getUserByGoogleUserId(userId);
		return user.getCurrentConversationAddress();
	}

	public static void updateCurrentConversationAddress(String userId,
			String newAddress) {
		User user = getUserByGoogleUserId(userId);
		user.setCurrentConversationAddress(newAddress);
		storeUser(user);
	}
}
