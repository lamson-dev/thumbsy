/*
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.lamson.thumbsy.models;

import javax.xml.bind.annotation.XmlRootElement;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * @author Son Nguyen
 */
@XmlRootElement
@Entity
public class User {

	public static final String ENTITY_NAME = "User";
	public static final String PROPERTY_ID = "userId";
	public static final String PROPERTY_GCM_REG_ID = "gcmRegId";
	public static final String PROPERTY_GOOGLE_ID = "googleUserId";
	public static final String PROPERTY_EMAIL = "email";
	public static final String PROPERTY_DISPLAY_NAME = "displayName";
	public static final String PROPERTY_PROFILE_URL = "profileUrl";
	public static final String PROPERTY_PHOTO_URL = "photoUrl";
	public static final String PROPERTY_CURRENT_CONVERSATION_ADDRESS = "currentConversationAddress";
	public static final String PROPERTY_DATE = "date";
	public static final String NO_CONVERSATION = "emptyField";

	@Id
	private Long id;
	@Index
	private String gcmRegId;
	@Index
	private String googleUserId;
	@Index
	private String displayName;
	@Index
	private String email;
	private String profileUrl;
	@Index
	private String photoUrl;
	private String coverUrl;
	private String currentConversationAddress;

	public User() {
	}

	public User(String gcmRegId, String googleUserId, String displayName) {
		this(null, gcmRegId, googleUserId, displayName, null, null, null, null,
				NO_CONVERSATION);
	}

	public User(String gcmRegId, String googleUserId, String displayName,
			String email, String profileUrl, String photoUrl, String coverUrl) {
		this(null, gcmRegId, googleUserId, displayName, email, profileUrl,
				photoUrl, coverUrl, NO_CONVERSATION);
	}

	public User(Long id, String gcmRegId, String googleUserId,
			String displayName, String email, String profileUrl,
			String photoUrl, String coverUrl, String currentConversationAddress) {
		this.setId(id);
		this.setGcmRegId(gcmRegId);
		this.setGoogleUserId(googleUserId);
		this.setDisplayName(displayName);
		this.setEmail(email);
		this.setProfileUrl(profileUrl);
		this.setPhotoUrl(photoUrl);
		this.setCoverUrl(coverUrl);
		this.setCurrentConversationAddress(currentConversationAddress);
	}

	// @Index
	// public String googleAccessToken;
	// public String googleRefreshToken;
	// public Long googleExpiresIn;
	// public Long googleExpiresAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGcmRegId() {
		return gcmRegId;
	}

	public void setGcmRegId(String gcmRegId) {
		this.gcmRegId = gcmRegId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getGoogleUserId() {
		return googleUserId;
	}

	public void setGoogleUserId(String googleUserId) {
		this.googleUserId = googleUserId;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profleUrl) {
		this.profileUrl = profleUrl;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getCurrentConversationAddress() {
		return currentConversationAddress;
	}

	public void setCurrentConversationAddress(String currentConversationAddress) {
		this.currentConversationAddress = currentConversationAddress;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

}
