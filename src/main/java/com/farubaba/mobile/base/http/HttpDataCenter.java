package com.farubaba.mobile.base.http;

import com.farubaba.mobile.base.http.model.IModel;
import com.farubaba.mobile.base.http.protocol.HttpClient;
import com.farubaba.mobile.base.http.protocol.RequestContext;

/**
 * 单例HttpDataCenter，可以通过设置不同的HTTPClient来实现不同的http控制；
 * 例如:readTimeout，cache，fore request，header 等控制
 * @author violet
 *
 */
public class HttpDataCenter implements HttpClient{
	
	private static HttpDataCenter instance = new HttpDataCenter();
	private HttpClient httpClient;
	private HttpDataCenter(){
		
	}
	
	public static HttpDataCenter getInstance(){
		return instance; 
	}
	
	public HttpDataCenter setHttpClient(HttpClient httpClient){
		if(httpClient == null){
			this.httpClient = new OkHttpManager();
		}else{
			this.httpClient = httpClient;
		}
		return this;
	}
	
	public HttpClient getHttpClient(){
		return this.httpClient;
	}

	@Override
	public <SUCESS extends IModel> void sendRequest(
			RequestContext<SUCESS> requestContext) {
		this.httpClient.sendRequest(requestContext);
	}
}
