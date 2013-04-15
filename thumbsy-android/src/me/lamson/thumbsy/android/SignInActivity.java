package me.lamson.thumbsy.android;

import me.lamson.thumbsy.android.PlusClientFragment.OnSignedInListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.plus.PlusClient;

/**
 */
public class SignInActivity extends BaseActivity implements
		View.OnClickListener, OnSignedInListener, ISignInView {

	private static final String TAG = SignInActivity.class.getSimpleName();

	private final SignInPresenter mPresenter;

	private TextView tvDisplay;

	public SignInActivity() {
		mPresenter = new SignInPresenter(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Log.d(TAG, TAG + " created");

		init();
	}

	private void init() {
		findViewById(R.id.btn_gplus_signin).setOnClickListener(this);
		tvDisplay = (TextView) findViewById(R.id.tv_display);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_gplus_signin:
			mSignInFragment.signIn(REQUEST_CODE_PLUS_CLIENT_FRAGMENT);
			break;
		}
	}

	@Override
	public void onSignedIn(PlusClient plusClient) {
		super.onSignedIn(plusClient);

		if (mPlusPerson != null) {
			mPresenter.setAppUser(mPlusPerson);
			enjoyMessaging();

		} else {
			Log.d(TAG, "error singing in google+");
			tvDisplay.setText("error in signing in google+");
		}
	}

	private void enjoyMessaging() {
		Intent intent = new Intent(SignInActivity.this, MainActivity.class);
		// intent.putExtra("isSignedIn", true);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		mSignInFragment.handleOnActivityResult(requestCode, responseCode,
				intent);
	}

	// TODO: put these options menu methods to BaseActivity!!!
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.options_clear:
			tvDisplay.setText(null);
			return true;
		case R.id.options_exit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public TextView getTvDisplay() {
		return tvDisplay;
	}

	public void setTvDisplay(TextView tvDisplay) {
		this.tvDisplay = tvDisplay;
	}

}
