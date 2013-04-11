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

import com.google.android.gms.plus.model.people.Person;

import android.app.Application;
import android.content.Context;

/**
 * Used to maintain global application state across PhotoHunt.
 */
public class ThumbsyApp extends Application {

	// private static final int HTTP_CACHE_SIZE = 10 * 1024 * 1024; // 10MB

	// Image cache size equals 1/3 device memory up to a maximum of 50MB
	// private static final long IMG_LOADER_CACHE_SIZE = Math.min(Runtime
	// .getRuntime().maxMemory() / 3, 50 * 1024 * 1024);

	// private ImageLoader mImageLoader;

	private static Context mContext;
	private static Person mUser;

	@Override
	public void onCreate() {
		super.onCreate();
		initialize();
	}

	private void initialize() {
		mContext = this;
		// Create a basic in-memory image cache. This should be replaced with a
		// file
		// response cache when used in production.
		// mImageLoader = new ImageLoader(IMG_LOADER_CACHE_SIZE);
		//
		// try {
		// File httpCacheDir = new File(getCacheDir(), "http");
		// Class.forName("android.net.http.HttpResponseCache")
		// .getMethod("install", File.class, long.class)
		// .invoke(null, httpCacheDir, HTTP_CACHE_SIZE);
		// } catch (Exception httpResponseCacheNotAvailable) {
		// // Ignore.
		// }
	}

	public static Context getContext() {
		return mContext;
	}

	public static void setUser(Person user) {
		ThumbsyApp.mUser = user;
	}

	public static Person getUser() {
		return mUser;
	}

}