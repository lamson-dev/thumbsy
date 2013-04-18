package me.lamson.thumbsy.appengine;

import static me.lamson.thumbsy.appengine.OfyService.ofy;

import java.util.List;

import me.lamson.thumbsy.models.Sms;
import me.lamson.thumbsy.models.SmsThread;

import com.googlecode.objectify.Key;

public class SmsDao {

	private final static int DEFAULT_LIMIT_MESSAGES = 50;

	public static void storeSms(Sms sms) {
		sms.setThreadKey(Key.create(SmsThread.class,
				sms.getUserId() + sms.getAddress()));
		ofy().save().entity(sms);
	}

	public static void deleteSms(Sms msg) {
		ofy().delete().entity(msg);
	}

	public static void deleteSmsById(Long id) {
		ofy().delete().type(Sms.class).id(id);
	}

	public static Sms getSmsById(Long id) {
		return ofy().load().type(Sms.class).id(id).get();
	}

	public static List<Sms> getSmsByThreadId(Long id) {
		return ofy().load().type(Sms.class).filter(Sms.PROPERTY_THREAD_ID, id)
				.limit(DEFAULT_LIMIT_MESSAGES).list();
	}

	public static List<Sms> getSmsByThreadKey(Key<SmsThread> key) {
		return ofy().load().type(Sms.class).filter("threadKey", key).list();
	}

	public static List<Sms> getSmsByThreadKey(String userId, String address) {
		Key<SmsThread> key = Key.create(SmsThread.class, userId + address);
		return ofy().load().type(Sms.class).filter("threadKey", key).list();
	}
}
