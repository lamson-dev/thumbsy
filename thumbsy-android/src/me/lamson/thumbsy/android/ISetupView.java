package me.lamson.thumbsy.android;

import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author Son Nguyen
 * 
 * @version $Revision: 1.0 $
 */
public interface ISetupView {

	public TextView getTvServerMessage();

	public void setTvServerMessage(TextView tvServerMessage);

	public TextView getTvDisplay();

	public void setTvDisplay(TextView tvDisplay);

	public TextView getTvSignedInStatus();

	public void setTvSignedInStatus(TextView tvSignedInStatus);

	public TextView getTvDeviceRegistered();

	public void setTvDeviceRegistered(TextView tvDeviceRegistered);

	public EditText getEtxtClientMessage();

	public void setEtxtClientMessage(EditText etxtClientMessage);

	public void sayAlreadyRegistered();
}
