package me.lamson.thumbsy.appengine.resources;

import com.google.gson.annotations.SerializedName;

/**
 * Request Java Bean - Custom request from user - User can make POST with a JSON
 * string, this POST then be redirected to GET to retrieve data with particular
 * parameters
 * 
 * @author Son Nguyen
 * 
 */
public class ApiRequest {

	@SerializedName("matched")
	private Boolean matched;
	@SerializedName("page")
	private int page;
	@SerializedName("perPage")
	private int perPage;
	@SerializedName("key")
	private String key;

	public ApiRequest() {
		this.setPage(1);
		this.setPerPage(5);
		this.setMatched(null);
		this.setKey(null);
	}

	public ApiRequest(Boolean matched, int page, int perPage, String key) {
		this.setMatched(matched);
		this.setPage(page);
		this.setPerPage(perPage);
		this.setKey(key);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPerPage() {
		return perPage;
	}

	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}

	public Boolean getMatched() {
		return matched;
	}

	public void setMatched(Boolean matched) {
		this.matched = matched;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
