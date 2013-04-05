/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.lamson.thumbsy.appengine;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that sends a message to a device.
 * <p>
 * This servlet is invoked by AppEngine's Push Queue mechanism.
 */
@SuppressWarnings("serial")
public class ReceiveMessageServlet extends BaseServlet {

	private static final String HEADER_QUEUE_COUNT = "X-AppEngine-TaskRetryCount";
	private static final String HEADER_QUEUE_NAME = "X-AppEngine-QueueName";
	private static final int MAX_RETRY = 3;

	static final String PARAMETER_DEVICE = "device";
	static final String PARAMETER_MULTICAST = "multicastKey";
	static final String PARAMETER_RECEIVED_MESSAGE = "receivedMessage";
	static final String PARAMETER_MESSAGE_OWNER = "messageOwner";
	static final String PARAMETER_ANDROID_USER = "androidUser";

	/**
	 * Indicates to App Engine that this task should be retried.
	 */
	private void retryTask(HttpServletResponse resp) {
		resp.setStatus(500);
	}

	/**
	 * Indicates to App Engine that this task is done.
	 */
	private void taskDone(HttpServletResponse resp) {
		resp.setStatus(200);
	}

	/**
	 * Processes the request to add a new message.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if (req.getHeader(HEADER_QUEUE_NAME) == null) {
			throw new IOException("Missing header " + HEADER_QUEUE_NAME);
		}
		String retryCountHeader = req.getHeader(HEADER_QUEUE_COUNT);
		logger.fine("retry count: " + retryCountHeader);
		if (retryCountHeader != null) {
			int retryCount = Integer.parseInt(retryCountHeader);
			if (retryCount > MAX_RETRY) {
				logger.severe("Too many retries, dropping task");
				taskDone(resp);
				return;
			}
		}
		String regId = req.getParameter(PARAMETER_DEVICE);
		if (regId != null) {
			String message = req.getParameter(PARAMETER_RECEIVED_MESSAGE);
			String owner = req.getParameter(PARAMETER_MESSAGE_OWNER);

			// sendSingleMessage(regId, resp, message);
			return;
		}
		logger.severe("Invalid request!");
		taskDone(resp);
		return;
	}

}
