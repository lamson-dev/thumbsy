/*
 */

package me.lamson.thumbsy.appengine;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.lamson.thumbsy.models.Conversation;
import me.lamson.thumbsy.models.Message;

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

		this.register(Conversation.class);
		this.register(Message.class);
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