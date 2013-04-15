/**
 * Application Launcher
 */

package me.lamson.thumbsy.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * @author Son Nguyen
 * @version $Revision: 1.0 $
 */
public class LauncherActivity extends Activity {
	private static final String TAG = LauncherActivity.class.getCanonicalName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// do something for app

		startActivity(new Intent(LauncherActivity.this, SignInActivity.class));
		Log.d(TAG, "finish launcher");
		finish();
	}
}
