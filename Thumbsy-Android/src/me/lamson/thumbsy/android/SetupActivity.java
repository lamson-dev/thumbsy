package me.lamson.thumbsy.android;

import static me.lamson.thumbsy.android.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static me.lamson.thumbsy.android.CommonUtilities.EXTRA_MESSAGE;
import static me.lamson.thumbsy.android.CommonUtilities.SENDER_ID;
import static me.lamson.thumbsy.android.CommonUtilities.SERVER_URL;
import me.lamson.thumbsy.android.PlusClientFragment.OnSignedInListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

/**
 * Example of signing in a user with Google+, and how to make a call to a
 * Google+ API endpoint.
 */
public class SetupActivity extends FragmentActivity implements
		View.OnClickListener, OnSignedInListener {

	public static final int REQUEST_CODE_PLUS_CLIENT_FRAGMENT = 0;

	private TextView mSignInStatus, mServerMessage, mDisplay;
	private EditText mClientMessage;
	private PlusClientFragment mSignInFragment;
	AsyncTask<Void, Void, Void> mRegisterTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		setContentView(R.layout.activity_setup);
		// setContentView(R.layout.main);

		mSignInFragment = PlusClientFragment.getPlusClientFragment(this,
				MomentUtil.VISIBLE_ACTIVITIES);

		findViewById(R.id.btn_gplus_signin).setOnClickListener(this);
		findViewById(R.id.btn_gplus_signout).setOnClickListener(this);
		findViewById(R.id.btn_gplus_revoke).setOnClickListener(this);
		findViewById(R.id.btn_send).setOnClickListener(this);
		mSignInStatus = (TextView) findViewById(R.id.tv_signin_status);
		mClientMessage = (EditText) findViewById(R.id.et_client_message);
		mServerMessage = (TextView) findViewById(R.id.tv_server_message);

		mDisplay = (TextView) findViewById(R.id.tv_display);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

	}

	private void registerDevice() {
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {

			// Automatically registers application on startup.
			GCMRegistrar.register(this, SENDER_ID);

		} else {

			// Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				mDisplay.append(getString(R.string.already_registered) + "\n");
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						boolean registered = ServerUtilities.register(context,
								regId);
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

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_gplus_signout:
			resetAccountState();
			mSignInFragment.signOut();
			break;
		case R.id.btn_gplus_signin:
			mSignInFragment.signIn(REQUEST_CODE_PLUS_CLIENT_FRAGMENT);
			break;
		case R.id.btn_gplus_revoke:
			resetAccountState();
			mSignInFragment.revokeAccessAndDisconnect();
			break;
		case R.id.btn_send:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		mSignInFragment.handleOnActivityResult(requestCode, responseCode,
				intent);
	}

	@Override
	public void onSignedIn(PlusClient plusClient) {

		// register device on signed in
		registerDevice();

		mSignInStatus.setText(getString(R.string.signed_in_status));

		// We can now obtain the signed-in user's profile information.
		Person currentPerson = plusClient.getCurrentPerson();
		if (currentPerson != null) {
			String greeting = getString(R.string.greeting_status,
					currentPerson.getDisplayName());
			mSignInStatus.setText(greeting);
		} else {
			resetAccountState();
		}
	}

	private void resetAccountState() {
		mSignInStatus.setText(getString(R.string.signed_out_status));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/*
		 * Typically, an application registers automatically, so options below
		 * are disabled. Uncomment them if you want to manually register or
		 * unregister the device (you will also need to uncomment the equivalent
		 * options on options_menu.xml).
		 */
		/*
		 * case R.id.options_register: GCMRegistrar.register(this, SENDER_ID);
		 * return true; case R.id.options_unregister:
		 * GCMRegistrar.unregister(this); return true;
		 */
		case R.id.options_clear:
			mDisplay.setText(null);
			return true;
		case R.id.options_exit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		unregisterReceiver(mHandleMessageReceiver);
		GCMRegistrar.onDestroy(this);
		super.onDestroy();
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			mDisplay.append(newMessage + "\n");
		}
	};
}
