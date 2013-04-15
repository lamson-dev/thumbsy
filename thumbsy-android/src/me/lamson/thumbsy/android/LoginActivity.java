package me.lamson.thumbsy.android;

import me.lamson.thumbsy.android.PlusClientFragment.OnSignedInListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

/**
 * Example of signing in a user with Google+, and how to make a call to a
 * Google+ API endpoint.
 */
public class LoginActivity extends BaseActivity implements
		View.OnClickListener, OnSignedInListener, ISetupView {

	private static final String TAG = LoginActivity.class.getCanonicalName();
	private final LoginPresenter mPresenter;

	private TextView tvSignedInStatus, tvServerMessage, tvDeviceRegistered,
			tvDisplay;
	private EditText etxtClientMessage;

	public LoginActivity() {
		mPresenter = new LoginPresenter(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		init();

		mPresenter.initGCM(this);
	}

	private void init() {
		findViewById(R.id.btn_gplus_signin).setOnClickListener(this);
		findViewById(R.id.btn_gplus_signout).setOnClickListener(this);
		findViewById(R.id.btn_gplus_revoke).setOnClickListener(this);
		findViewById(R.id.btn_send).setOnClickListener(this);
		tvSignedInStatus = (TextView) findViewById(R.id.tv_signin_status);
		tvDeviceRegistered = (TextView) findViewById(R.id.tv_device_register);
		etxtClientMessage = (EditText) findViewById(R.id.et_client_message);
		tvServerMessage = (TextView) findViewById(R.id.tv_server_message);
		tvDisplay = (TextView) findViewById(R.id.tv_display);
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

			// unregister device on revoke
			mPresenter.unregisterDevice(this);

			break;
		case R.id.btn_send:
			mPresenter.sendMessage(Long.valueOf(1));
			break;
		}
	}

	@Override
	public void onSignedIn(PlusClient plusClient) {
		super.onSignedIn(plusClient);

		tvSignedInStatus.setText(getString(R.string.signed_in_status));

		if (mPlusPerson != null) {
			String greeting = getString(R.string.greeting_status,
					mPlusPerson.getDisplayName());
			tvSignedInStatus.setText(greeting);

			mPresenter.setAppUser(mPlusPerson);
			mPresenter.registerDevice(this);
		} else {
			resetAccountState();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		mSignInFragment.handleOnActivityResult(requestCode, responseCode,
				intent);
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
			tvDisplay.setText("");
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
		mPresenter.cleanUpGCM(this);
		super.onDestroy();
	}

	public void resetAccountState() {
		this.tvSignedInStatus.setText(getString(R.string.signed_out_status));
	}

	public void sayAlreadyRegistered() {
		tvDeviceRegistered.setText(getString(R.string.already_registered));
	}

	public TextView getTvServerMessage() {
		return tvServerMessage;
	}

	public void setTvServerMessage(TextView tvServerMessage) {
		this.tvServerMessage = tvServerMessage;
	}

	public TextView getTvDisplay() {
		return tvDisplay;
	}

	public void setTvDisplay(TextView tvDisplay) {
		this.tvDisplay = tvDisplay;
	}

	public TextView getTvSignedInStatus() {
		return tvServerMessage;
	}

	public void setTvSignedInStatus(TextView tvSignedInStatus) {
		this.tvSignedInStatus = tvSignedInStatus;
	}

	public TextView getTvDeviceRegistered() {
		return tvDeviceRegistered;
	}

	public void setTvDeviceRegistered(TextView tvDeviceRegistered) {
		this.tvDeviceRegistered = tvDeviceRegistered;

	}

	public EditText getEtxtClientMessage() {
		return etxtClientMessage;
	}

	public void setEtxtClientMessage(EditText etxtClientMessage) {
		this.etxtClientMessage = etxtClientMessage;
	}

}
