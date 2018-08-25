package com.farubaba.mobile.base.http.protocol;

/**
 * 定义常见的http请求，例如： get,post,返回json，java对象，集合等。
 * @author violet
 *
 */
public interface HttpClient {
	public void sendRequest(RequestContext requestContext);
}
