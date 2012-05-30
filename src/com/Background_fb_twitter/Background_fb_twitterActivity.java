package com.Background_fb_twitter;


import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Background_fb_twitterActivity extends Activity {
	private Button btn_fb;
	private Button btn_tw;
	private Button btn_share;

	/** face book details */
	static final String APP_ID = "facebook app id";
	static final String[] PERMISSIONS = new String[] { "publish_stream" };
	static final String TOKEN = "access_token";
	static final String EXPIRES = "expires_in";
	static final String KEY = "facebook-credentials";
	static Facebook facebook1;
	private boolean FB_LOGIN = false;

	/** ---------------------------------- */

	private boolean TWEET = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btn_fb = (Button) findViewById(R.id.btn_fb);
		btn_tw = (Button) findViewById(R.id.btn_twitter);
		btn_share = (Button) findViewById(R.id.btn_share);

		btn_fb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				facebook1 = new Facebook(APP_ID);
				loginAndPostToWall();
			}
		});

		btn_tw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						PrepareRequestTokenActivity.class);
				i.putExtra("tweet_msg", "");
				startActivity(i);
				TWEET = true;
			}
		});

		btn_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Background_fb_twitterActivity.this, share_text.class));
				/*if (FB_LOGIN && TWEET) {
					//show_alert();
					
				}
				else
				{
					Toast.makeText(Background_fb_twitterActivity.this,
							"Pleaes Login To facebook & Twitter!", Toast.LENGTH_SHORT)
							.show();
				}*/
			}
		});
	}

	

	/** facebook methods---------------------------------------------start */
	public void loginAndPostToWall() {
		facebook1.authorize(this, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH,
				new LoginDialogListener());

	}

	class LoginDialogListener implements DialogListener {

		public void onComplete(Bundle values) {
			saveCredentials(facebook1);
			FB_LOGIN = true;
		}

		public void onFacebookError(FacebookError error) {
			showToast("Authentication with Facebook failed!");
		}

		public void onError(DialogError error) {
			showToast("Authentication with Facebook failed!");
		}

		public void onCancel() {
			showToast("Authentication with Facebook cancelled!");
		}
	}

	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	public boolean saveCredentials(Facebook facebook) {

		Editor editor = getApplicationContext().getSharedPreferences(KEY,
				Context.MODE_PRIVATE).edit();
		editor.putString(TOKEN, facebook.getAccessToken());
		editor.putLong(EXPIRES, facebook.getAccessExpires());
		return editor.commit();
	}

	public boolean restoreCredentials(Facebook facebook) {
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(KEY, Context.MODE_PRIVATE);

		facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
		facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
		return facebook.isSessionValid();
	}

	/** facebook methods---------------------------------------------end */
	
	
}