package me.lamson.thumbsy.android;

import java.util.HashSet;
import java.util.Set;

import me.lamson.thumbsy.models.Conversation;
import me.lamson.thumbsy.models.Message;
import static me.lamson.thumbsy.android.CommonUtils.SENDER_ID;
import static me.lamson.thumbsy.android.CommonUtils.SERVER_URL;
import static me.lamson.thumbsy.android.CommonUtils.EXTRA_MESSAGE;
import static me.lamson.thumbsy.android.CommonUtils.DISPLAY_MESSAGE_ACTION;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.plus.model.people.Person;

/**
 * 
 * @author Son Nguyen
 * @version $Revision: 1.0 $
 */
public class SetupPresenter {

	private static final String TAG = SetupPresenter.class.getCanonicalName();

	public static final String URL_POST_MESSAGE = "http://thumbsy-demo.appspot.com/message";
	public static final String URL_POST_CONVERSATION = "http://thumbsy-demo.appspot.com/conversation";

	private final ISetupView mView;

	private final Set<Long> conversationsOnServer = new HashSet<Long>();

	AsyncTask<Void, Void, Void> mRegisterTask, mSendMessageTask,
			mSendConversationTask;

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			mView.getTvDisplay().append(newMessage + "\n");
		}
	};

	public SetupPresenter(ISetupView view) {
		mView = view;
	}

	public void initGCM(Context context) {
		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(context);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(context);

		context.registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
	}

	public void cleanUpGCM(Context context) {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		context.unregisterReceiver(mHandleMessageReceiver);
		GCMRegistrar.onDestroy(context);
	}

	private final boolean postJsonData(String url, String json) {

		HttpClient client = new DefaultHttpClient();

		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
		HttpResponse response;

		try {
			HttpPost post = new HttpPost(url);
			StringEntity se = new StringEntity(json);
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			post.setEntity(se);
			response = client.execute(post);

			// HttpPost request = new HttpPost(serverUrl);
			// request.setEntity(new ByteArrayEntity(
			// postMessage.toString().getBytes("UTF8")));
			// HttpResponse response = client.execute(request);

			/* Checking response */
			if (response != null) {
				Log.d(TAG, EntityUtils.toString(response.getEntity()));
			}

			return (response.getStatusLine().getStatusCode() == 200);

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Cannot Estabilish Connection");
		}
		return false;

	}

	public void sendMessage() {
		final Long conversationId = Long.valueOf(200);

		if (!conversationsOnServer.contains(conversationId)) {
			final String conversationData = createConversation(conversationId);

			mSendConversationTask = new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					if (postJsonData(URL_POST_CONVERSATION, conversationData))
						conversationsOnServer.add(conversationId);
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					mSendConversationTask = null;
				}

			};
			mSendConversationTask.execute(null, null, null);
		}

		final String messageData = createMessage(null, conversationId, mView
				.getEtxtClientMessage().getText().toString());

		mSendMessageTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				postJsonData(URL_POST_MESSAGE, messageData);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				mSendMessageTask = null;
			}

		};
		mSendMessageTask.execute(null, null, null);
	}

	public void registerDevice(final Context context) {

		final String regId = GCMRegistrar.getRegistrationId(context);
		final String userId = ThumbsyApp.getUser().getId();

		if (regId.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(context, SENDER_ID);
		} else {

			// Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(context)) {
				// Skips registration.
				mView.sayAlreadyRegistered();
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				// registerDeviceOnServer();

				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						boolean registered = ServerUtils.register(context,
								regId, userId);
						// At this point all attempts to register with the app
						// server failed, so we need to unregister the device
						// from GCM - the app will try to register again when
						// it is restarted. Note that GCM will send an
						// unregistered callback upon completion, but
						// GCMIntentService.onUnregistered() will ignore it.
						if (!registered) {
							GCMRegistrar.unregister(context);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}

	public void unregisterDevice(Context context) {
		GCMRegistrar.unregister(context);

		// maybe unregister from server as well?
	}

	public void setAppUser(Person user) {
		ThumbsyApp.setUser(user);
	}

	private String createConversation(Long id) {
		Conversation c = new Conversation(id, ThumbsyApp.getUser().getId(),
				"no content");
		return CommonUtils.GSON.toJson(c);
	}

	private String createMessage(Long id, Long conversationId, String message) {
		Message m = new Message(id, conversationId, message, false);
		return CommonUtils.GSON.toJson(m);
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(ThumbsyApp.getContext()
					.getResources().getString(R.string.error_config, name));
		}
	}

}