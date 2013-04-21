package me.lamson.thumbsy.appengine.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import me.lamson.thumbsy.appengine.dao.SmsThreadDao;
import me.lamson.thumbsy.models.SmsThread;

/**
 * 
 * @author Son Nguyen
 * 
 */
@Path("/conversations")
public class SmsThreadResource extends BaseResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Path("{userId : \\d+}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<SmsThread> getThreadsByUserId(@PathParam("userId") String userId) {
		return SmsThreadDao.getThreadsByUserId(userId);
	}
}