/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.lamson.thumbsy.android;

import me.lamson.thumbsy.android.PlusClientFragment.OnSignedInListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

/**
 * Manages the authentication using Google sign-in and the PhotoHunt back end
 * service across all activities.
 * 
 * Authentication using Google sign-in is delegated to
 * {@link PlusClientFragment} which responds via the
 * {@link #onSignedIn(PlusClient)} method to indicated successful authentication
 * and the {@link #onSignInFailed()} method to indicate failure.
 * 
 * On successful authentication the corresponding OAuth token is passed to the
 * PhotoHunt back end service to be associated with a service specific profile.
 */
public abstract class BaseActivity extends FragmentActivity implements
		OnSignedInListener, View.OnClickListener {

	/**
	 * Code used to identify the login request to the {@link PlusClientFragment}
	 * .
	 */
	public static final int REQUEST_CODE_PLUS_CLIENT_FRAGMENT = 0;

	/** Delegate responsible for handling Google sign-in. */
	protected PlusClientFragment mSignInFragment;

	/** Used to retrieve the PhotoHunt back end session id. */
	// private AsyncTask<Object, Void, User> mAuthTask;

	/** Person as returned by Google Play Services. */
	protected Person mPlusPerson;

	/** Profile as returned by the PhotoHunt service. */
	// protected User mAppUser;

	/**
	 * Stores the @link com.google.android.gms.common.SignInButton} for use in
	 * the action bar.
	 */
	protected SignInButton mSignInButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getSupportActionBar().setDisplayShowHomeEnabled(false);
		// mPhotoClient = new PhotoClient();
		// mImageLoader = ((PhotoHuntApp) getApplication()).getImageLoader();

		// Create the PlusClientFragment which will initiate authentication if
		// required.
		// AuthUtil.SCOPES describe the permissions that we are requesting of
		// the user to access
		// their information and write to their moments vault.
		// AuthUtil.VISIBLE_ACTIVITIES describe the types of moment which we can
		// read from or write
		// to the user's vault.
		mSignInFragment = PlusClientFragment.getPlusClientFragment(this,
				MomentUtil.VISIBLE_ACTIVITIES);

		// mSignInButton = (SignInButton) getLayoutInflater().inflate(
		// R.layout.sign_in_button, null);
		// mSignInButton.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		super.onActivityResult(requestCode, responseCode, intent);

		// Delegate onActivityResult handling to PlusClientFragment to resolve
		// authentication
		// failures, eg. if the user must select an account or grant our
		// application permission to
		// access the information we have requested in AuthUtil.SCOPES and
		// AuthUtil.VISIBLE_ACTIVITIES
		mSignInFragment.handleOnActivityResult(requestCode, responseCode,
				intent);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	/**
	 * Invoked when the {@link PlusClientFragment} delegate has successfully
	 * authenticated the user.
	 * 
	 * @param plusClient
	 *            The connected PlusClient which gives us access to the Google+
	 *            APIs.
	 */
	@Override
	public void onSignedIn(PlusClient plusClient) {
		if (plusClient.isConnected()) {

			// We can now obtain the signed-in user's profile information.
			mPlusPerson = plusClient.getCurrentPerson();

			// Retrieve the account name of the user which allows us to retrieve
			// the OAuth access
			// token that we securely pass over to the PhotoHunt service to
			// identify and
			// authenticate our user there.
			final String name = plusClient.getAccountName();

			// Asynchronously authenticate with the PhotoHunt service and
			// retrieve the associated
			// PhotoHunt profile for the user.
			// mAuthTask = new AsyncTask<Object, Void, User>() {
			// @Override
			// protected User doInBackground(Object... o) {
			// return AuthUtil.authenticate(BaseActivity.this, name);
			// }
			//
			// @Override
			// protected void onPostExecute(User result) {
			// if (result != null) {
			// setAuthenticatedProfile(result);
			// executePendingActions();
			// update();
			// } else {
			// setAuthenticatedProfile(null);
			// mPlus.signOut();
			// }
			// }
			// };
			//
			// mAuthTask.execute();
		}
	}

	/**
	 * Update the user interface to reflect the current application state. This
	 * function is called whenever this Activity's state has been modified.
	 * 
	 * {@link BaseActivity} calls this method when user authentication succeeds
	 * or fails.
	 */
	public void update() {
		supportInvalidateOptionsMenu();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// getSupportActionBar().setDisplayShowCustomEnabled(false);

		// if (isAuthenticated()) {
		// // Show the 'sign out' menu item only if we have an authenticated
		// // PhotoHunt profile.
		// menu.add(0, R.id.menu_item_sign_out, 0,
		// getString(R.string.sign_out_menu_title)).setShowAsAction(
		// MenuItem.SHOW_AS_ACTION_NEVER);
		//
		// menu.add(0, R.id.menu_item_disconnect, 0,
		// getString(R.string.disconnect_menu_title)).setShowAsAction(
		// MenuItem.SHOW_AS_ACTION_NEVER);
		// } else if (!isAuthenticated() && !isAuthenticating()) {
		// ActionBar.LayoutParams params = new ActionBar.LayoutParams(
		// ActionBar.LayoutParams.WRAP_CONTENT,
		// ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
		// getSupportActionBar().setCustomView(mSignInButton, params);
		// getSupportActionBar().setDisplayShowCustomEnabled(true);
		// }

		return true;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button) {
			mSignInFragment.signIn(REQUEST_CODE_PLUS_CLIENT_FRAGMENT);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// switch (item.getItemId()) {
		// case R.id.menu_item_disconnect:
		// mPhotoClient.disconnectAccount(new FetchCallback<Void>() {
		// @Override
		// public void onSuccess(Void result) {
		// mPlus.signOut();
		// // Invalidate the PhotoHunt session
		// AuthUtil.invalidateSession();
		// }
		//
		// @Override
		// public void onError(Void result) {
		// Toast toast = Toast.makeText(BaseActivity.this,
		// getString(R.string.disconnect_failed),
		// Toast.LENGTH_LONG);
		// toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		// toast.show();
		// }
		// });
		//
		// return true;
		//
		// case R.id.menu_item_sign_out:
		// mPlus.signOut();
		// // Invalidate the PhotoHunt session
		// AuthUtil.invalidateSession();
		// return true;
		// }

		return super.onOptionsItemSelected(item);
	}

}
