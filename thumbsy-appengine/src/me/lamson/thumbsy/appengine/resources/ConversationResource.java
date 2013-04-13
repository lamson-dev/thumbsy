package me.lamson.thumbsy.appengine.resources;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

/**
 * Brand Resource - Return responses for HTTP requests for a Brand and Modify
 * relational database via Brand Data Access Object
 * 
 * @author Son Nguyen
 * 
 */
@Path("/conversation")
// @Produces(MediaType.APPLICATION_JSON)
public class ConversationResource {
	// @Context
	// UriInfo uriInfo;
	// @Context
	// Request request;
	//
	// Long id;
	//
	// public ConversationResource(UriInfo uriInfo, Request request, String id)
	// {
	// this.uriInfo = uriInfo;
	// this.request = request;
	// this.id = Long.valueOf(id);
	// }

	// @GET
	// public ApiResponse getConversation() {
	//
	// ApiResponse result = new ApiResponse();
	//
	// if (!BrandDao.instance.hasBrand(id)) {
	// result.setResponse(ResStatusMsg.brandNotFound());
	// throw new BrandNotFoundException(Tools.gsonPretty.toJson(result));
	// } else {
	// result.add(BrandDao.instance.get(id));
	// result.setResponse(ResStatusMsg.ok());
	// }
	//
	// return result;
	// }
	//
	// @PUT
	// @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
	// MediaType.TEXT_PLAIN })
	// public Response putBrand(String json) {
	// String jsonRes;
	//
	// if (BrandDao.instance.hasBrand(id)) {
	// jsonRes = Tools.gsonPretty.toJson(ResStatusMsg.brandExisted());
	// return Response.status(Response.Status.NOT_ACCEPTABLE)
	// .entity(jsonRes).build();
	// }
	//
	// try {
	// Brand brand = Tools.gsonPretty.fromJson(json, Brand.class);
	// if (brand.getId() == -1)
	// brand.setId(id);
	// BrandDao.instance.add(brand);
	//
	// jsonRes = Tools.gsonPretty.toJson(ResStatusMsg.created());
	// return Response.status(Response.Status.CREATED).entity(jsonRes)
	// .build();
	//
	// } catch (Exception e) {
	// jsonRes = Tools.gsonPretty.toJson(ResStatusMsg.wrongJsonFormat());
	// return Response.status(Response.Status.NOT_ACCEPTABLE)
	// .entity(jsonRes).build();
	// }
	// }
	//
	// @POST
	// @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
	// MediaType.TEXT_PLAIN })
	// public Response postBrand(String json) {
	//
	// if (!BrandDao.instance.hasBrand(id))
	// return this.putBrand(json);
	//
	// try {
	// Brand b = Tools.gsonPretty.fromJson(json, Brand.class);
	//
	// Brand brand = BrandDao.instance.get(id);
	//
	// if (!b.getName().equals("Unknown"))
	// brand.setName(b.getName());
	// if (b.getLogoUrls() != null)
	// brand.setLogoUrls(b.getLogoUrls());
	// if (b.getSiteUrls() != null)
	// brand.setSiteUrls(b.getSiteUrls());
	// if (b.getqScore() != -1)
	// brand.setqScore(b.getqScore());
	//
	// brand.setMatched(b.isMatched());
	// BrandDao.instance.update(brand);
	// String jsonRes = Tools.gsonPretty.toJson(ResStatusMsg.updated());
	// return Response.ok(jsonRes).build();
	//
	// } catch (Exception e) {
	// return Response.status(Status.NOT_ACCEPTABLE)
	// .entity(ResStatusMsg.wrongJsonFormat()).build();
	// }
	//
	// }
	//
	// @DELETE
	// public Response deleteBrand() {
	// String jsonRes;
	// if (!BrandDao.instance.hasBrand(id)) {
	// jsonRes = Tools.gsonPretty.toJson(ResStatusMsg.brandNotFound());
	// return Response.status(Response.Status.NOT_FOUND).entity(jsonRes)
	// .build();
	// }
	//
	// BrandDao.instance.delete(id);
	// jsonRes = Tools.gsonPretty.toJson(ResStatusMsg.deleted());
	// return Response.ok(jsonRes).build();
	// }
}