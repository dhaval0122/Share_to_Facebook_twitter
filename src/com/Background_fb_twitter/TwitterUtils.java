package com.Background_fb_twitter;

import oauth.signpost.OAuth;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import android.content.SharedPreferences;
import android.util.Log;

public class TwitterUtils {

	public static boolean isAuthenticated(SharedPreferences prefs) {

		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

		AccessToken a = new AccessToken(token, secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);

		try {
			twitter.getAccountSettings();
			return true;
		} catch (TwitterException e) {
			return false;
		}
	}

	public static void sendTweet(SharedPreferences prefs, String msg)
			throws Exception {
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		Log.d("dhaval-> token", token);
		Log.d("dhaval-> secret", secret);
		Log.d("dhaval-> msg", msg);

		try {
			AccessToken a = new AccessToken(token, secret);
			Log.d("acc_token :", "" + a);
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(Constants.CONSUMER_KEY,
					Constants.CONSUMER_SECRET);
			twitter.setOAuthAccessToken(a);
			twitter.updateStatus(""+msg);
		} catch (Exception e) { // TODO: handleexception
			Log.d("error dj-->", e.getMessage().toString());
		}

		/*try {
			TwitterFactory factory = new TwitterFactory();
			Twitter twitter = factory.getInstance();
			//AccessToken accestoken = new AccessToken(token, secret);
			AccessToken accestoken = new AccessToken("hx6GH2dEk0APOeMKCBof1g", "UTufsnbngxKojsbEEfwLbCoqoSjSk3SeVEMge9YZk0");

			twitter.setOAuthAccessToken(accestoken);
			Status status = twitter.updateStatus(msg);
			System.out.println("it worked!");
			if (status.getId() == 0) {
				System.out
						.println("Error occured while posting tweets to twitter");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("error dj-->", e.getMessage().toString());
		}*/

	}
}
