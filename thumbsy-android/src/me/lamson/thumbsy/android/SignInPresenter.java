package me.lamson.thumbsy.android;

import com.google.android.gms.plus.model.people.Person;

/**
 * 
 * @author Son Nguyen
 * @version $Revision: 1.0 $
 */
public class SignInPresenter {

	private static final String TAG = SignInPresenter.class.getSimpleName();

	private final ISignInView mView;

	public SignInPresenter(ISignInView view) {
		mView = view;
	}

	public void setAppUser(Person user) {
		ThumbsyApp.setUser(user);
	}

}