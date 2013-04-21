package me.lamson.thumbsy.appengine.resources;

import java.util.logging.Logger;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

/**
 * 
 * @author Son Nguyen
 * 
 */
@Produces(MediaType.APPLICATION_JSON)
public class BaseResource {

	protected final static Gson GSON = new Gson();
	protected final Logger logger = Logger.getLogger(getClass().getName());
}