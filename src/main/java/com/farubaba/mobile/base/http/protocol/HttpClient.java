package com.farubaba.mobile.base.http.protocol;

import com.farubaba.mobile.base.http.model.IModel;

/**
 * 定义常见的http请求，例如： get,post,返回json，java对象，集合等。
 * @author violet
 *
 */
public interface HttpClient {
	public <SUCESS extends IModel>void sendRequest(RequestContext<SUCESS> requestContext);
}
