package me.lamson.thumbsy.appengine.resources;

import java.util.logging.Logger;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

/**
 * Brands Resource - Return responses for HTTP requests for a number of brands
 * 
 * @author Son Nguyen
 * 
 */
@Produces(MediaType.APPLICATION_JSON)
public class BaseResource {

	final static Gson GSON = new Gson();
	protected final Logger logger = Logger.getLogger(getClass().getName());
}