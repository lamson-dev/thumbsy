package me.lamson.thumbsy.android;

import static me.lamson.thumbsy.android.CommonUtils.DISPLAY_MESSAGE_ACTION;
import static me.lamson.thumbsy.android.CommonUtils.RECEIVE_SMS_ACTION;
import static me.lamson.thumbsy.android.CommonUtils.SENDER_ID;
import static me.lamson.thumbsy.android.CommonUtils.SERVER_URL;
import static me.lamson.thumbsy.android.CommonUtils.URL_POST_MESSAGE;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import me.lamson.thumbsy.models.Sms;
import me.lamson.thumbsy.models.SmsThread;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

/**
 * 
 * @author Son Nguyen
 * @version $Revision: 1.0 $
 */
public class MainPresenter {

	private static final String TAG = MainPresenter.class.getSimpleName();
	private final IMainView mView;
	private final Context mContext;
	private final Set<Long> conversationsOnServer = new HashSet<Long>();

	AsyncTask<Void, Void, Void> mRegisterTask, mSendMessageTask,
			mSendConversationTask;

	private boolean isExisted = false;

	public MainPresenter(IMainView view, Context context) {
		mView = view;
		mContext = context;
	}

	private final BroadcastReceiver mHandleGCMMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String msgBody = intent.getExtras().getString(Sms.PROPERTY_BODY);
			String msgAddress = intent.getExtras().getString(
					Sms.PROPERTY_ADDRESS);

			// display message on screen
			mView.getTvDisplay().append(msgBody + "\n");

			// send SMS
			sendSMS(msgAddress, msgBody);
		}
	};

	private final BroadcastReceiver mHandleSMSReceiver = new BroadcastReceiver() {

		// Retrieve SMS
		public void onReceive(Context context, Intent intent) {

			Long msgId = Long.valueOf(intent.getExtras()
					.getInt(Sms.PROPERTY_ID));
			String msgBody = intent.getExtras().getString(Sms.PROPERTY_BODY);
			String msgAddress = intent.getExtras().getString(
					Sms.PROPERTY_ADDRESS);
			String msgDate = intent.getExtras().getString(Sms.PROPERTY_DATE);

			Sms msg = new Sms(msgId, msgBody, msgAddress, true, msgDate);

			mView.getTvDisplay().append(msgBody + "\n" + msgAddress);

			if (msgAddress.equals("+12052085117"))
				sendMessageToServer(msg);
		}

	};

	/**
	 * initialize GCM, check whether device could register for GCM or not, if
	 * good then register a messageReceiver
	 * 
	 * @param context
	 */
	public void initGCM(Context context) {
		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(context);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(context);

		context.registerReceiver(mHandleGCMMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		context.registerReceiver(mHandleSMSReceiver, new IntentFilter(
				RECEIVE_SMS_ACTION));
	}

	public void cleanUpGCM(Context context) {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		context.unregisterReceiver(mHandleGCMMessageReceiver);
		context.unregisterReceiver(mHandleSMSReceiver);
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

	public void sendMessageToServer(final Sms msg) {

		// should I check for thread on server?
		msg.setUserId(ThumbsyApp.getUser().getId());
		final String smsData = CommonUtils.GSON.toJson(msg);

		Log.d(TAG, smsData);

		mSendMessageTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				if (postJsonData(URL_POST_MESSAGE, smsData)) {
					Log.d(TAG, "message posted to " + URL_POST_MESSAGE);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				mSendMessageTask = null;
			}

		};
		mSendMessageTask.execute(null, null, null);
	}

	private static String SENT = "SMS_SENT";
	private static String DELIVERED = "SMS_DELIVERED";
	private static int MAX_SMS_MESSAGE_LENGTH = 160;

	private void sendSMS(String phoneNumber, String msgBody) {

		PendingIntent piSent = PendingIntent.getBroadcast(mView.getContext(),
				0, new Intent(SENT), 0);
		PendingIntent piDelivered = PendingIntent.getBroadcast(
				mView.getContext(), 0, new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		mContext.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					// Toast.makeText(getBaseContext(), "SMS sent",
					// Toast.LENGTH_SHORT).show();
					Log.d(TAG, "SMS sent");
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					// Toast.makeText(getBaseContext(), "Generic failure",
					// Toast.LENGTH_SHORT).show();
					Log.d(TAG, "Generic failure");
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					// Toast.makeText(getBaseContext(), "No service",
					// Toast.LENGTH_SHORT).show();
					Log.d(TAG, "No service");
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					// Toast.makeText(getBaseContext(), "Null PDU",
					// Toast.LENGTH_SHORT).show();
					Log.d(TAG, "null PDU");
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					// Toast.makeText(getBaseContext(), "Radio off",
					// Toast.LENGTH_SHORT).show();
					Log.d(TAG, "Radio off");
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		mContext.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					// Toast.makeText(getBaseContext(), "SMS delivered",
					// Toast.LENGTH_SHORT).show();
					Log.d(TAG, "SMS delivered");
					break;
				case Activity.RESULT_CANCELED:
					// Toast.makeText(getBaseContext(), "SMS not delivered",
					// Toast.LENGTH_SHORT).show();
					Log.d(TAG, "SMS not delivered");
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		// TODO: after sending SMS, should write to inbox as well
		SmsManager smsManager = SmsManager.getDefault();

		int length = msgBody.length();
		if (length > MAX_SMS_MESSAGE_LENGTH) {
			ArrayList<String> messagelist = smsManager.divideMessage(msgBody);
			smsManager.sendMultipartTextMessage(phoneNumber, null, messagelist,
					null, null);
		} else
			smsManager.sendTextMessage(phoneNumber, null, msgBody, piSent,
					piDelivered);
	}

	// public boolean checkExistingConversation(final Long id) {
	//
	// mSendConversationTask = new AsyncTask<Void, Void, Void>() {
	//
	// @Override
	// protected Void doInBackground(Void... params) {
	// HttpClient client = new DefaultHttpClient();
	// HttpConnectionParams.setConnectionTimeout(client.getParams(),
	// 10000); // Timeout
	// HttpResponse response;
	//
	// try {
	// HttpGet get = new HttpGet(URL_CHECK_CONVERSATION
	// + String.valueOf(id));
	// response = client.execute(get);
	//
	// /* Checking response */
	// if (response != null) {
	// Log.d(TAG, EntityUtils.toString(response.getEntity()));
	// }
	//
	// isExisted = (response.getStatusLine().getStatusCode() == 200);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e(TAG, "Cannot Estabilish Connection");
	// }
	// isExisted = false;
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void result) {
	// mSendConversationTask = null;
	// }
	//
	// };
	// mSendConversationTask.execute(null, null, null);
	//
	// return isExisted;
	//
	// }

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
	}

	private String createConversation(Long id) {
		SmsThread c = new SmsThread(id, ThumbsyApp.getUser().getId(),
				"no content");
		return CommonUtils.GSON.toJson(c);
	}

	private String createMessage(Long msgId, String msgBody, String msgAddress) {
		Sms sms = new Sms(msgId, msgBody, msgAddress, true,
				String.valueOf(new Date().toString()));
		sms.setUserId(ThumbsyApp.getUser().getId());
		return CommonUtils.GSON.toJson(sms);
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(ThumbsyApp.getContext()
					.getResources().getString(R.string.error_config, name));
		}
	}

}