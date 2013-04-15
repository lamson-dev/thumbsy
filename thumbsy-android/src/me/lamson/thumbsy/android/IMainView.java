package me.lamson.thumbsy.android;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author Son Nguyen
 * 
 * @version $Revision: 1.0 $
 */
public interface IMainView {

	public TextView getTvServerMessage();

	public void setTvServerMessage(TextView tvServerMessage);

	public TextView getTvDisplay();

	public void setTvDisplay(TextView tvDisplay);

	public TextView getTvSignedInStatus();

	public void setTvSignedInStatus(TextView tvSignedInStatus);

	public TextView getTvDeviceRegistered();

	public void setTvDeviceRegistered(TextView tvDeviceRegistered);

	public EditText getEditClientMessage();

	public void setEditClientMessage(EditText etxtClientMessage);

	public void sayAlreadyRegistered();

	public Context getContext();
}
