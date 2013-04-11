// Copyright 2011, Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package me.lamson.thumbsy.appengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.lamson.thumbsy.models.Conversation;
import me.lamson.thumbsy.models.Message;

import com.google.appengine.api.datastore.Entity;

/**
 * This servlet responds to the request corresponding to items. The class
 * creates and manages the ItemEntity
 * 
 * 
 */
@SuppressWarnings("serial")
public class MessageServlet extends BaseServlet {

	static final String PARAMETER_JSON_DATA = "jsonData";

	private static final Logger logger = Logger.getLogger(MessageServlet.class
			.getCanonicalName());

	/**
	 * Searches for the entity based on the search criteria and returns result
	 * in JSON format
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		super.doGet(req, resp);
		logger.log(Level.INFO, "Obtaining Message listing");

		String searchBy = req.getParameter("message-searchby");
		String searchFor = req.getParameter("conversationId");

		PrintWriter out = resp.getWriter();
		if (searchFor == null || searchFor.equals("")) {

			Iterable<Entity> entities = MessageDao.getAllMessages();
			out.println(DatastoreUtils.writeJSON(entities));

		} else if (searchBy == null || searchBy.equals("messageId")) {

			Iterable<Entity> entities = MessageDao.getMessage(Long
					.valueOf(searchFor));
			out.println(DatastoreUtils.writeJSON(entities));

		} else if (searchBy != null && searchBy.equals("conversationId")) {

			Iterable<Entity> entities = MessageDao.getMessagesForConversation(
					"Message", Long.valueOf(searchFor));

			out.println(DatastoreUtils.writeJSON(entities));
		}
	}

	/**
	 * Creates entity and persists the same
	 */
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.log(Level.INFO, "Creating Message");
		// String jsonData = req.getParameter(PARAMETER_JSON_DATA);

		PrintWriter out = resp.getWriter();

		// String jsonData = req.getParameter(PARAMETER_JSON_DATA);
		String jsonData = readJsonRequest(req);
		try {
			Message message = GSON.fromJson(jsonData, Message.class);
			MessageDao.createOrUpdateMessage(message);
		} catch (Exception e) {
			String msg = DatastoreUtils.getErrorMessage(e);
			out.print(msg);
		}
	}

	/**
	 * Delete the entity from the datastore. Throws an exception if there are
	 * any orders associated with the item and ignores the delete action for it.
	 */
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.log(Level.INFO, "deleting message");
		Long messageId = Long.parseLong(req.getParameter("id"));
		PrintWriter out = resp.getWriter();
		try {
			out.println(MessageDao.deleteMessage(messageId));
		} catch (Exception e) {
			out.println(DatastoreUtils.getErrorMessage(e));
		}
	}

	/**
	 * Redirects to delete or insert entity based on the action in the HTTP
	 * request.
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// String action = req.getParameter("action");
		// if (action.equalsIgnoreCase("delete")) {
		// doDelete(req, resp);
		// return;
		// } else if (action.equalsIgnoreCase("put")) {
		// doPut(req, resp);
		// return;
		// }

		doPut(req, resp);
		return;
	}
}