package com.farubaba.mobile.base.http;

import com.farubaba.mobile.base.http.model.IModel;
import com.farubaba.mobile.base.http.protocol.HttpAdapter;
import com.farubaba.mobile.base.http.protocol.RequestContext;

/**
 * 单例HttpDataCenter，可以通过设置不同的HTTPClient来实现不同的http控制；
 * 例如:readTimeout，cache，fore request，header 等控制
 * @author violet
 *
 */
public class HttpManager implements HttpAdapter{
	
	private static HttpManager instance = new HttpManager();
	private HttpAdapter httpAdapter;
	private HttpManager(){
		setHttpAdapter(new OkHttpAdapter());
	}
	
	public static HttpManager getInstance(){
		return instance; 
	}
	
	public HttpAdapter getHttpAdapter() {
		return httpAdapter;
	}

	public HttpManager setHttpAdapter(HttpAdapter httpAdapter) {
		if(httpAdapter != null){
			this.httpAdapter = new OkHttpAdapter();
		}
		return this;
	}

	@Override
	public <M extends IModel> void sendRequest(
			RequestContext<M> requestContext) {
		getHttpAdapter().sendRequest(requestContext);
	}
}
