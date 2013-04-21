package me.lamson.thumbsy.appengine.dao;

import static me.lamson.thumbsy.appengine.OfyService.ofy;

import java.util.Date;
import java.util.List;

import me.lamson.thumbsy.models.Sms;
import me.lamson.thumbsy.models.SmsThread;

import com.googlecode.objectify.Key;

public class SmsDao {

	private final static int DEFAULT_LIMIT_MESSAGES = 20;

	public static void storeSms(Sms sms) {
		Key<SmsThread> key = Key.create(SmsThread.class,
				sms.getAddress() + sms.getUserId());
		sms.setThreadKey(key);
		sms.setDate(new Date().getTime());
		ofy().save().entity(sms);
	}

	public static void deleteSms(Sms msg) {
		ofy().delete().entity(msg);
	}

	public static void deleteSmsById(long id) {
		ofy().delete().type(Sms.class).id(id);
	}

	public static Sms getSmsById(long id) {
		return ofy().load().type(Sms.class).id(id).get();
	}

	public static List<Sms> getSmsByThreadKey(Key<SmsThread> key) {
		return ofy().load().type(Sms.class)
				.filter(Sms.PROPERTY_THREAD_KEY, key)
				.order("-" + Sms.PROPERTY_DATE).limit(DEFAULT_LIMIT_MESSAGES)
				.list();
	}

	public static List<Sms> getSmsByThreadKey(String userId, String address) {
		Key<SmsThread> key = Key.create(SmsThread.class, address + userId);
		return getSmsByThreadKey(key);
	}

	public static List<Sms> getSmsByThreadId(long id) {
		return ofy().load().type(Sms.class).filter(Sms.PROPERTY_THREAD_ID, id)
				.order(Sms.PROPERTY_DATE).limit(DEFAULT_LIMIT_MESSAGES).list();
	}
}
