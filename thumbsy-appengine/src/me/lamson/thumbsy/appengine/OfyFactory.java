/*
 */

package me.lamson.thumbsy.appengine;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.lamson.thumbsy.models.Sms;
import me.lamson.thumbsy.models.SmsThread;
import me.lamson.thumbsy.models.User;

import com.google.inject.Injector;
import com.googlecode.objectify.ObjectifyFactory;

/**
 * Our version of ObjectifyFactory which integrates with Guice. You could and
 * convenience methods here too.
 * 
 * @author Jeff Schnitzer
 */
@Singleton
// @Slf4j
public class OfyFactory extends ObjectifyFactory {
	/** */
	@Inject
	private static Injector injector;

	/** Register our entity types */
	public OfyFactory() {
		// long time = System.currentTimeMillis();

		this.register(SmsThread.class);
		this.register(Sms.class);
		this.register(User.class);
		// this.register(EmailLookup.class);

		// long millis = System.currentTimeMillis() - time;
		// log.info("Registration took " + millis + " millis");
	}

	/** Use guice to make instances instead! */
	@Override
	public <T> T construct(Class<T> type) {
		return super.construct(type);
		// return injector.getInstance(type);
	}

	@Override
	public Ofy begin() {
		return new Ofy(super.begin());
	}
}