// $codepro.audit.disable com.instantiations.assist.eclipse.analysis.audit.rule.effectivejava.alwaysOverridetoString.alwaysOverrideToString
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
	/**
	 * Field TAG. (value is ""Launcher"")
	 */
	private static final String TAG = "Launcher";

	/**
	 * open the database when application starts
	 * 
	 * @param savedInstanceState
	 *            Bundle
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// do something for app

		// final DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,
		// "spacetrader-db", null);
		// SpaceTrader.db = helper.getWritableDatabase(); //
		// $codepro.audit.disable assignmentToNonFinalStatic
		// SpaceTrader.daoMaster = new DaoMaster(SpaceTrader.db); //
		// $codepro.audit.disable assignmentToNonFinalStatic
		// SpaceTrader.daoSession = SpaceTrader.daoMaster.newSession(); //
		// $codepro.audit.disable assignmentToNonFinalStatic

		final Intent intent = new Intent(LauncherActivity.this,
				LoginActivity.class);
		startActivity(intent);
		// Log.d(TAG, "open database");
		Log.d(TAG, "finish launcher");
		finish();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "finish launcher");
		finish();
	}
}
