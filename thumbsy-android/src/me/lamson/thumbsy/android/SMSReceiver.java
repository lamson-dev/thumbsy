package me.lamson.thumbsy.android;

import static me.lamson.thumbsy.android.CommonUtils.RECEIVE_SMS_ACTION;
import me.lamson.thumbsy.models.Sms;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
	private final String TAG = getClass().getSimpleName().toString();
	private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private Context mContext;
	private Intent mIntent;

	// Retrieve SMS
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		mIntent = intent;

		String action = intent.getAction();

		if (action.equals(ACTION_SMS_RECEIVED)) {

			String msgAddress = "";
			String msgBody = "";
			String msgDate = "";

			int msgId = -1;
			int contactId = -1;

			SmsMessage[] msgs = getMessagesFromIntent(mIntent);
			if (msgs != null) {
				for (int i = 0; i < msgs.length; i++) {

					msgId = msgs[i].getIndexOnIcc();
					msgAddress = msgs[i].getOriginatingAddress();
					msgDate = String.valueOf(msgs[i].getTimestampMillis());
					// contactId = ContactsUtils.getContactId(mContext, address,
					// "address");
					msgBody += msgs[i].getMessageBody().toString();
					msgBody += "\n";
				}
			}

			if (contactId != -1) {
				showNotification(contactId, msgBody);
			}

			// ---send a broadcast intent to update the SMS received in the
			// activity---
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(RECEIVE_SMS_ACTION);
			broadcastIntent.putExtra(Sms.PROPERTY_ID, msgId);
			broadcastIntent.putExtra(Sms.PROPERTY_BODY, msgBody);
			broadcastIntent.putExtra(Sms.PROPERTY_ADDRESS, msgAddress);
			broadcastIntent.putExtra(Sms.PROPERTY_DATE, msgDate);
			context.sendBroadcast(broadcastIntent);

			// _id, thread_id, address, person, date, protocol, read, status,
			// type, reply_path_present, subject, body, service_center, locked

		}

	}

	public static SmsMessage[] getMessagesFromIntent(Intent intent) {
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}
		return msgs;
	}

	/**
	 * The notification is the icon and associated expanded entry in the status
	 * bar.
	 */
	protected void showNotification(int contactId, String message) {
		// Display notification...
	}
}