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
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.lamson.thumbsy.models.Conversation;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;

/**
 * This servlet responds to the request corresponding to conversation entities.
 * The servlet manages the Product Entity
 * 
 * 
 */
@SuppressWarnings("serial")
public class ConversationServlet extends BaseServlet {

	private static final Logger logger = Logger
			.getLogger(ConversationServlet.class.getCanonicalName());

	/**
	 * Get the entities in JSON format.
	 */

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
		logger.log(Level.INFO, "Obtaining conversation listing");
		
		String searchFor = req.getParameter("id");
		
		PrintWriter out = resp.getWriter();
		Iterable<Entity> entities = null;
		if (searchFor == null || searchFor.equals("") || searchFor == "*") {
			entities = ConversationDao
					.getAllConversations(Conversation.ENTITY_NAME);
			out.println(DatastoreUtils.writeJSON(entities));
		} else {
			Entity conversation = ConversationDao.getConversation(Long
					.parseLong(searchFor));
			if (conversation != null) {
				Set<Entity> result = new HashSet<Entity>();
				result.add(conversation);
				out.println(DatastoreUtils.writeJSON(result));
			}
		}
	}

	/**
	 * Create the entity and persist it.
	 */
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.log(Level.INFO, "Creating conversation");
		PrintWriter out = resp.getWriter();

		// String jsonData = req.getParameter(PARAMETER_JSON_DATA);
		String jsonData = readJsonRequest(req);
		try {
			Conversation conversation = GSON.fromJson(jsonData,
					Conversation.class);

			ConversationDao.createOrUpdateConversation(conversation);
		} catch (Exception e) {
			String msg = DatastoreUtils.getErrorMessage(e);
			out.print(msg);
		}
	}

	/**
	 * Delete the conversation entity
	 */
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String conversationkey = req.getParameter("id");
		PrintWriter out = resp.getWriter();
		try {
			out.println(ConversationDao.deleteConversation(Long
					.parseLong(conversationkey)));
		} catch (Exception e) {
			out.println(DatastoreUtils.getErrorMessage(e));
		}
	}

	/**
	 * Redirect the call to doDelete or doPut method
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
		
		doPut(req,resp);
		return;
	}

}