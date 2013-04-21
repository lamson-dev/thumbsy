/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.lamson.thumbsy.android;

import static me.lamson.thumbsy.android.CommonUtils.GSON;
import static me.lamson.thumbsy.android.CommonUtils.SERVER_URL;
import static me.lamson.thumbsy.android.CommonUtils.TAG;
import static me.lamson.thumbsy.android.CommonUtils.displayMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import me.lamson.thumbsy.models.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.plus.model.people.Person;

/**
 * Helper class used to communicate with the demo server.
 */
public final class ServerUtils {

	private static final int MAX_ATTEMPTS = 5;
	private static final int BACKOFF_MILLI_SECONDS = 2000;
	private static final Random random = new Random();

	/**
	 * Register this account/device pair within the server.
	 * 
	 * @return whether the registration succeeded or not.
	 */
	static boolean register(final Context context, final String regId) {
		Log.i(TAG, "registering device (regId = " + regId + ")");

		// device is registered with GCM
		// now register user on thumbsy server

		Person p = ThumbsyApp.getUser();

		String displayName = (p.hasDisplayName()) ? p.getDisplayName() : null;
		String email = (p.hasEmails()) ? p.getEmails().get(0).getValue() : null;
		String profileUrl = (p.hasUrl()) ? p.getUrl() : null;
		String photoUrl = (p.hasImage()) ? p.getImage().getUrl() : null;
		String coverUrl = (p.hasCover()) ? p.getCover().getCoverPhoto()
				.getUrl() : null;

		User user = new User(regId, p.getId(), displayName, email, profileUrl,
				photoUrl, coverUrl);

		String serverUrl = SERVER_URL + "/register";

		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);
		params.put("googleUserId", p.getId());

		String jsonData = GSON.toJson(user);

		Log.d(TAG, jsonData);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register it in the
		// demo server. As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			// try {
			displayMessage(context, context.getString(
					R.string.server_registering, i, MAX_ATTEMPTS));

			if (postJsonData(serverUrl, jsonData)) {
				GCMRegistrar.setRegisteredOnServer(context, true);
				String message = context.getString(R.string.server_registered);
				CommonUtils.displayMessage(context, message);
				return true;
			} else {
				// Here we are simplifying and retrying on any error; in a real
				// // application, it should retry only on unrecoverable errors
				// // (like HTTP error code 503).
				// Log.e(TAG, "Failed to register on attempt " + i, e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return false;
				}
				// increase backoff exponentially
				backoff *= 2;
			}

			// post(serverUrl, params);
			// GCMRegistrar.setRegisteredOnServer(context, true);
			// String message = context.getString(R.string.server_registered);
			// CommonUtils.displayMessage(context, message);
			// return true;
			// } catch (IOException e) {
			// // Here we are simplifying and retrying on any error; in a real
			// // application, it should retry only on unrecoverable errors
			// // (like HTTP error code 503).
			// Log.e(TAG, "Failed to register on attempt " + i, e);
			// if (i == MAX_ATTEMPTS) {
			// break;
			// }
			// try {
			// Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
			// Thread.sleep(backoff);
			// } catch (InterruptedException e1) {
			// // Activity finished before we complete - exit.
			// Log.d(TAG, "Thread interrupted: abort remaining retries!");
			// Thread.currentThread().interrupt();
			// return false;
			// }
			// // increase backoff exponentially
			// backoff *= 2;
			// }
		}
		String message = context.getString(R.string.server_register_error,
				MAX_ATTEMPTS);
		CommonUtils.displayMessage(context, message);
		return false;
	}

	/**
	 * Unregister this account/device pair within the server.
	 */
	static void unregister(final Context context, final String regId) {
		Log.i(TAG, "unregistering device (regId = " + regId + ")");
		String serverUrl = SERVER_URL + "/unregister";
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);
		try {
			post(serverUrl, params);
			GCMRegistrar.setRegisteredOnServer(context, false);
			String message = context.getString(R.string.server_unregistered);
			CommonUtils.displayMessage(context, message);
		} catch (IOException e) {
			// At this point the device is unregistered from GCM, but still
			// registered in the server.
			// We could try to unregister again, but it is not necessary:
			// if the server tries to send a message to the device, it will get
			// a "NotRegistered" error message and should unregister the device.
			String message = context.getString(
					R.string.server_unregister_error, e.getMessage());
			CommonUtils.displayMessage(context, message);
		}
	}

	static boolean postJsonData(String url, String json) {

		HttpClient client = new DefaultHttpClient();

		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
		HttpResponse response;

		try {
			HttpPost post = new HttpPost(url);
			StringEntity se = new StringEntity(json);
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			post.setEntity(se);
			response = client.execute(post);

			// HttpPost request = new HttpPost(serverUrl);
			// request.setEntity(new ByteArrayEntity(
			// postMessage.toString().getBytes("UTF8")));
			// HttpResponse response = client.execute(request);

			/* Checking response */
			if (response != null) {
				Log.d(TAG, EntityUtils.toString(response.getEntity()));
			}

			return (response.getStatusLine().getStatusCode() == 200 || response
					.getStatusLine().getStatusCode() == 201);

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Cannot Estabilish Connection");
		}
		return false;

	}

	/**
	 * Issue a POST request to the server.
	 * 
	 * @param endpoint
	 *            POST address.
	 * @param params
	 *            request parameters.
	 * 
	 * @throws IOException
	 *             propagated from POST.
	 */
	private static void post(String endpoint, Map<String, String> params)
			throws IOException {
		URL url;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		Log.v(TAG, "Posting '" + body + "' to " + url);
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
}
