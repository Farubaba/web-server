package com.farubaba.mobile.base.http;

import com.farubaba.mobile.base.http.protocol.HttpClient;
import com.farubaba.mobile.base.http.protocol.RequestContext;

import okhttp3.OkHttpClient;

/**
 * http默认实现是使用okhttp，如果需要更换实现，这仿造该类建立新的实现，设置给httpDataCenter即可。
 * 易拔插，易替换。
 * 
 * @author violet
 *
 */
public class OkHttpClientImpl implements HttpClient {
	
	private OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
	
	private OkHttpClient okHttpClient = builder.build(); 
	
	public OkHttpClient getOkHttpClient() {
		return okHttpClient;
	}

	public OkHttpClientImpl setOkHttpClient(OkHttpClient okHttpClient) {
		this.okHttpClient = okHttpClient;
		return this;
	}

	@Override
	public void sendRequest(RequestContext requestContext) {
		
	}
}
