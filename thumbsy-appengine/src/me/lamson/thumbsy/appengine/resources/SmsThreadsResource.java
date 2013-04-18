//package me.lamson.thumbsy.appengine.resources;
//
//import java.util.List;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Request;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.UriInfo;
//
//import me.lamson.thumbsy.appengine.SmsThreadDao;
//import me.lamson.thumbsy.models.Conversation;
//
///**
// * Brands Resource - Return responses for HTTP requests for a number of brands
// * 
// * @author Son Nguyen
// * 
// */
//// Will map the resource to the URL brands
//@Path("/conversations")
//public class SmsThreadsResource extends BaseResource {
//
//	// Allows to insert contextual objects into the class,
//	// e.g. ServletContext, Request, Response, UriInfo
//	@Context
//	UriInfo uriInfo;
//	@Context
//	Request request;
//
//	@GET
//	@Path("{conversationId : \\d+}")
//	// support digits only
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	public Conversation getConversation(
//			@PathParam("conversationId") String conversationId) {
//
//		Conversation conversation = SmsThreadDao.getThreadById(Long
//				.valueOf(conversationId));
//		if (conversation == null) {
//			throw new RuntimeException("Get: Todo with " + conversationId
//					+ " not found");
//		}
//		return conversation;
//	}
//
//	@GET
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	public List<Conversation> getAllConversations() {
//		return SmsThreadDao.getAllThreads();
//	}
//
//	@GET
//	@Path("count")
//	@Produces(MediaType.TEXT_PLAIN)
//	public String getCount() {
//		// int count = TodoDao.instance.getModel().size();
//		// return String.valueOf(count);
//		return null;
//	}
//
//	@PUT
//	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	public Response putConversation(String jsonData) {
//
//		// String jsonResponse = "";
//		try {
//			Conversation conv = GSON.fromJson(jsonData, Conversation.class);
//			SmsThreadDao.storeThread(conv);
//			return Response.status(Response.Status.CREATED).build();
//
//		} catch (Exception e) {
//			// return Response.status(Response.Status.NOT_ACCEPTABLE)
//			// .entity(jsonResponse).build();
//			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
//
//		}
//	}
//
//	@POST
//	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	public Response postConversation(String jsonData) {
//
//		// String jsonResponse = "";
//		try {
//			Conversation conv = GSON.fromJson(jsonData, Conversation.class);
//			SmsThreadDao.storeThread(conv);
//			return Response.status(Response.Status.CREATED).build();
//
//		} catch (Exception e) {
//			// return Response.status(Response.Status.NOT_ACCEPTABLE)
//			// .entity(jsonResponse).build();
//			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
//
//		}
//	}
//
//	// @POST
//	// @Produces(MediaType.TEXT_HTML)
//	// @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	// public void newTodo(@FormParam("id") String id,
//	// @FormParam("summary") String summary,
//	// @FormParam("description") String description,
//	// @Context HttpServletResponse servletResponse) throws IOException {
//	// Todo todo = new Todo(id, summary);
//	// if (description != null) {
//	// todo.setDescription(description);
//	// }
//	// TodoDao.instance.getModel().put(id, todo);
//	//
//	// servletResponse.sendRedirect("../create_todo.html");
//	// }
//
//	// Allows to insert contextual objects into the class,
//	// e.g. ServletContext, Request, Response, UriInfo
//	// @Context
//	// UriInfo uriInfo;
//	// @Context
//	// Request request;
//	//
//	// private final boolean DEFAULT_MATCHED = false;
//	// private final int DEFAULT_PER_PAGE = 5;
//	// private final int DEFAULT_PAGE = 1;
//	// private final String SECRET_KEY = "popshops";
//
//	// @GET
//	// public ApiResponse getBrands(@QueryParam("matched") Boolean matched,
//	// @QueryParam("page") Integer page,
//	// @QueryParam("perPage") Integer perPage,
//	// @QueryParam("key") String key) {
//	// ApiResponse result = new ApiResponse();
//	// if (key == null || !key.equals(SECRET_KEY)) {
//	// result.setResponse(ResStatusMsg.unauthorized());
//	// throw new BrandNotFoundException(Tools.gson.toJson(result));
//	// }
//	// if (matched == null)
//	// matched = DEFAULT_MATCHED;
//	// if (page == null)
//	// page = DEFAULT_PAGE;
//	// if (perPage == null)
//	// perPage = DEFAULT_PER_PAGE;
//	//
//	// result.setBrands(BrandDao.instance.getBrands(matched, page, perPage));
//	// if (result.getBrands().isEmpty()) {
//	// result.setResponse(ResStatusMsg.emptyBrands());
//	// throw new EmptyBrandsException(Tools.gsonPretty.toJson(result));
//	// } else
//	// result.setResponse(ResStatusMsg.ok());
//	// return result;
//	// }
//	//
//	// @GET
//	// @Path("count")
//	// public String getCountMatchedBrands(@QueryParam("matched") Boolean
//	// matched) {
//	// if (matched == null) {
//	// matched = this.DEFAULT_MATCHED;
//	// }
//	// int count = BrandDao.instance.countBrands(matched);
//	// return String.valueOf(count);
//	// }
//	//
//	// @POST
//	// public ApiResponse getBrands(String jsonRequest) {
//	//
//	// ApiResponse result = new ApiResponse();
//	// ApiRequest req;
//	// try {
//	// req = Tools.gson.fromJson(jsonRequest, ApiRequest.class);
//	// } catch (Exception e) {
//	// result.setResponse(ResStatusMsg.wrongJsonFormat());
//	// throw new WrongJsonFormatException(Tools.gsonPretty.toJson(result));
//	// }
//	// return this.getBrands(req.getMatched(), req.getPage(),
//	// req.getPerPage(), req.getKey());
//	//
//	// }
//	//
//	// @PUT
//	// @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
//	// MediaType.TEXT_PLAIN })
//	// public Response putBrand(String json) {
//	// String jsonRes;
//	//
//	// try {
//	// Brand brand = Tools.gsonPretty.fromJson(json, Brand.class);
//	// if (BrandDao.instance.hasBrand(brand.getId())) {
//	// jsonRes = Tools.gsonPretty.toJson(ResStatusMsg.brandExisted());
//	// return Response.status(Response.Status.NOT_ACCEPTABLE)
//	// .entity(jsonRes).build();
//	// }
//	//
//	// BrandDao.instance.add(brand);
//	//
//	// jsonRes = Tools.gsonPretty.toJson(ResStatusMsg.created());
//	// return Response.status(Response.Status.CREATED).entity(jsonRes)
//	// .build();
//	//
//	// } catch (Exception e) {
//	// jsonRes = Tools.gsonPretty.toJson(ResStatusMsg.wrongJsonFormat());
//	// return Response.status(Response.Status.NOT_ACCEPTABLE)
//	// .entity(jsonRes).build();
//	// }
//	// }
//	//
//	// /*
//	// * Defines that the next path parameter after brands is treated as a
//	// * parameter and passed to the BrandResources - Allows to type
//	// * http://popbrands.herokuapp.com/v1/brands/1 1 will be treaded as
//	// parameter
//	// * brand and passed to BrandResource
//	// */
//	// @Path("{id}")
//	// public MessageResource getBrand(@PathParam("id") int id,
//	// @QueryParam("key") String key) {
//	// if (key == null || !key.equals(SECRET_KEY)) {
//	// ApiResponse result = new ApiResponse();
//	// result.setResponse(ResStatusMsg.unauthorized());
//	// throw new BrandNotFoundException(Tools.gson.toJson(result));
//	// }
//	// return new MessageResource(uriInfo, request, id);
//	//
//	// }
// }