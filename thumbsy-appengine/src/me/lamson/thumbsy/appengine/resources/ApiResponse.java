package me.lamson.thumbsy.appengine.resources;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Custom response from API for GET method - Return response and brand data
 * 
 * @author Son Nguyen
 * 
 */
@XmlRootElement(namespace = "com.popshops.brandapi.models")
@XmlType(propOrder = { "response", "brands" })
public class ApiResponse {

	// @SerializedName("response")
	// private ResStatusMsg response;
	// @SerializedName("brands")
	// private List<Brand> brands;
	//
	// public ApiResponse() {
	// this.setBrands(new ArrayList<Brand>());
	// this.setResponse(new ResStatusMsg());
	// }
	//
	// public List<Brand> getBrands() {
	// return brands;
	// }
	//
	// public void setBrands(List<Brand> brands) {
	// this.brands = brands;
	// }
	//
	// public void add(Brand brand) {
	// this.brands.add(brand);
	// }
	//
	// public ResStatusMsg getResponse() {
	// return response;
	// }
	//
	// public void setResponse(ResStatusMsg response) {
	// this.response = response;
	// }

}
