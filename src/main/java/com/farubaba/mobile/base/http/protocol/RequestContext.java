package com.farubaba.mobile.base.http.protocol;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

/**
 * 
 * URL
 * method
 * header   key-value
 * headers  midiMap
 * parameter key-value
 * body     any string 
 * body 	stream
 * MultiPart
 * stream
 * callback
 * response code
 * response header
 * response body
 * 
 * @author violet
 *
 */
public class RequestContext implements IRequestContext {
	public static final String CHARSET = "utf-8";
	public static final String PROTOCOL_CONTENT_TYPE_JSON = String.format("application/json; charset=%s", CHARSET);
    public static String PROTOCOL_CONTENT_TYPE_X_WWW_FORM_URLENCODED = String.format("application/x-www-form-urlencoded; charset=%s", CHARSET);
    
	public String host;
	public String url;
	public HttpMethod method = HttpMethod.GET;
	/**
	 * request header
	 * 该集合一个key只能对应一个value
	 */
	public Map<String,String> header = new HashMap<String, String>();
	/**
	 * request headers
	 * 该集合一个key可以对应多个value
	 */
	public ListMultimap<String,String> headers = MultimapBuilder.hashKeys().arrayListValues().build();
	/**
	 * request query parameters
	 */
	public Map<String,String> querys = new HashMap<String, String>();
	//request body
	//MultiPart
	//stream
	//callback
	//response code
	//response header
	//response body
	public String getHost() {
		return host;
	}
	
	public RequestContext setHost(String host) {
		this.host = host;
		return this;
	}
	public String getUrl() {
		return url;
	}
	public RequestContext setUrl(String url) {
		this.url = url;
		return this;
	}
	public HttpMethod getMethod() {
		return method;
	}
	public RequestContext setMethod(HttpMethod method) {
		this.method = method;
		return this;
	}
	public Map<String, String> getHeader() {
		return header;
	}
	public RequestContext setHeader(Map<String, String> header) {
		this.header = header;
		return this;
	}
	public ListMultimap<String, String> getHeaders() {
		return headers;
	}
	public RequestContext setHeaders(ListMultimap<String, String> headers) {
		this.headers = headers;
		return this;
	}
	public Map<String, String> getQuerys() {
		return querys;
	}
	public RequestContext setQuerys(Map<String, String> querys) {
		this.querys = querys;
		return this;
	}
	
}
