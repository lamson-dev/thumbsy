/**
 * Copyright 2011 Google
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
package me.lamson.thumbsy.appengine;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelFailureException;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

/**
 * This servlet is responsible for sending messages across the channel.
 * 
 * @author Son Nguyen
 */
@SuppressWarnings("serial")
public class NotifyServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(NotifyServlet.class
			.getCanonicalName());
	private static ChannelService channelService = ChannelServiceFactory
			.getChannelService();

	/**
	 * Check the incoming parameters and create the channel message . Send
	 * "OFFLINE" reply in case of an exception such as the user channel do not
	 * exist
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("userId");

		String address = request.getParameter("address");

		if (user != null && !user.equals("") && address != null
				&& !address.equals("")) {
			try {
				String outputMessage = "<data>" + "<type>updateChatBox</type>"
						+ "<address>" + address + "</address>" + "<user>"
						+ user + "</user>" + "</data>";

				logger.log(Level.INFO, "sending address into the channel");

				sendMessageToChannel(user, outputMessage);

			} catch (ChannelFailureException channelFailure) {
				logger.log(Level.WARNING,
						"Failed in sending address to channel");
				response.getWriter().print("OFFLINE");
			} catch (Exception e) {
				logger.log(Level.WARNING,
						"Unknow error while sending address to the channel");
				response.getWriter().print("OFFLINE");
			}
		}
	}

	/**
	 * Creates the Channel Message and sends to the client
	 * 
	 * @param user
	 *            the user to whom the message is sent
	 * @param address
	 *            the address that needs to pass
	 */
	public void sendMessageToChannel(String user, String address)
			throws ChannelFailureException {
		channelService.sendMessage(new ChannelMessage(user, address));
	}
}