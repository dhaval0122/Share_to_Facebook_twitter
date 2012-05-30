package com.Background_fb_twitter;


import com.facebook.android.Facebook;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class share_text extends Activity {

	private EditText edit;
	private Button btns;

	/** face book details */
	static final String APP_ID = "facebook app id";
	static final String[] PERMISSIONS = new String[] { "publish_stream" };
	static final String TOKEN = "access_token";
	static final String EXPIRES = "expires_in";
	static final String KEY = "facebook-credentials";
	static Facebook facebook1;
	private boolean FB_LOGIN = false;

	/** ---------------------------------- */
	
	private SharedPreferences prefs1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_alert);
		this.prefs1 = PreferenceManager.getDefaultSharedPreferences(this);

		edit = (EditText) findViewById(R.id.editText1);
		btns = (Button) findViewById(R.id.button1);
		btns.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = edit.getText().toString();
				if (str != "") {

					postToWall(str, share_text.this);

					if (TwitterUtils.isAuthenticated(prefs1)) {
					sendTweet(share_text.this, str);
					}

				} else {
					edit.setError("Please Enter Text!");
				}

			}
		});
	}

	public static void postToWall(String message, Context con) {
		facebook1 = new Facebook(APP_ID);
		String st = get_token__(con);
		Bundle parameters = new Bundle();
		parameters.putString("message", message);
		parameters.putString("description", "topic share");
		if (st.length() > 0) {
			parameters.putString("access_token", "" + st);
		}
		try {
			facebook1.request("me");
			String response = facebook1.request("me/feed", parameters, "POST");
			Log.d("Tests--->*************", "got response: " + response);

			if (response == null
					|| response.equals("")
					|| response.equals("false")
					|| response
							.equalsIgnoreCase("{\"error\":{\"message\":\"An active access token must be used to query information about the current user.\",\"type\":\"OAuthException\",\"code\":2500}}")) {
				showToast("Blank response. please loginf again in facebook",
						con);
				clear_fb_data(con);
			} else {
				showToast("Message posted to your facebook wall!", con);
			}
		} catch (Exception e) {
			showToast("Failed to post to wall!", con);
			e.printStackTrace();
		}
	}

	private static String get_token__(Context con) {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = con.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		return sharedPreferences.getString(TOKEN, null);
	}

	private static void clear_fb_data(Context con) {
		// TODO Auto-generated method stub
		SharedPreferences se = PreferenceManager
				.getDefaultSharedPreferences(con);
		Editor editor = se.edit();
		editor.remove(TOKEN);
		editor.remove(EXPIRES);
		editor.commit();
	}

	private static void showToast(String message, Context con) {
		Toast.makeText(con, message, Toast.LENGTH_SHORT).show();
	}

	/** twitter methods-------------------------- start */

	private final static Handler mTwitterHandler = new Handler();
	private static SharedPreferences prefs;

	public static boolean TWEET_LOGIN = false;

	final static Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {

		}
	};

	public static void sendTweet(Context con, final String msj) {

		prefs = PreferenceManager.getDefaultSharedPreferences(con);

		Thread t = new Thread() {
			public void run() {

				try {
					TwitterUtils.sendTweet(prefs, msj);
					mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
					Log.d("dhaval-->send tweet:", ex.getMessage().toString());
				}
			}

		};
		t.start();
	}

	/** twitter methods-------------------------- end */
}
