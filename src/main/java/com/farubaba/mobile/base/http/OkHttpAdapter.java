package com.farubaba.mobile.base.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.farubaba.mobile.base.http.model.IModel;
import com.farubaba.mobile.base.http.protocol.HttpAdapter;
import com.farubaba.mobile.base.http.protocol.HttpMethod;
import com.farubaba.mobile.base.http.protocol.IHttpCallback;
import com.farubaba.mobile.base.http.protocol.RequestContext;
import com.farubaba.mobile.base.json.JsonFactory;
import com.farubaba.mobile.base.json.JsonService;
import com.farubaba.mobile.base.util.ConcurrentUtil;
import com.farubaba.mobile.server.model.ErrorResult;
import com.google.common.collect.ListMultimap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

/**
 * http默认实现是使用okhttp，如果需要更换实现，这仿造该类建立新的实现，设置给httpDataCenter即可。
 * 易拔插，易替换。
 * 
 * @author violet
 *
 */
public class OkHttpAdapter implements HttpAdapter{
	
	private OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder();
	
	private OkHttpClient okHttpClient = okHttpClientBuilder
			.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888)))
			.build(); 
	
	private JsonService<?> jsonService = JsonFactory.getJsonService();
	
	public OkHttpClient getOkHttpClient() {
		return okHttpClient;
	}

	public OkHttpAdapter setOkHttpClient(OkHttpClient okHttpClient) {
		this.okHttpClient = okHttpClient;
		return this;
	}
	
	private <M extends IModel> Request.Builder prepareRequestBuilder(RequestContext<M> requestContext){
		Request.Builder requestBuilder = new Request.Builder();
		setHeader(requestContext, requestBuilder);
		addHeaders(requestContext, requestBuilder);
		HttpMethod method = requestContext.getMethod();
		switch(method){
			case GET:
				return prepareGet(requestContext,requestBuilder);
			case POST:
				return preparePost(requestContext,requestBuilder);
			case PUT:
				return preparePut(requestContext,requestBuilder);
			case HEAD:
				return prepareHead(requestContext,requestBuilder);
			case DELETE:
				return prepareDelete(requestContext,requestBuilder);
			case CONNECT:
				return prepareConnect(requestContext,requestBuilder);
			case TRACE:
				return prepareTrace(requestContext,requestBuilder);
			case OPTIONS:
				return prepareOptions(requestContext,requestBuilder);
			default:
				return prepareGet(requestContext,requestBuilder);
			
		}
	}
	
	private <M extends IModel> Builder prepareGet(RequestContext<M> requestContext, Builder requestBuilder) {
		requestBuilder.url(requestContext.getUrl());	
		return requestBuilder;
	}


	private <M extends IModel> Builder preparePost(RequestContext<M> requestContext, Builder requestBuilder) {
		// TODO Auto-generated method stub
		return null;
	}

	private <M extends IModel> Builder preparePut(RequestContext<M> requestContext, Builder requestBuilder) {
		// TODO Auto-generated method stub
		return null;
	}

	private <M extends IModel> Builder prepareHead(RequestContext<M> requestContext, Builder requestBuilder) {
		// TODO Auto-generated method stub
		return null;
	}

	private <M extends IModel>  Builder prepareDelete(RequestContext<M> requestContext, Builder requestBuilder) {
		// TODO Auto-generated method stub
		return null;
	}

	private <M extends IModel> Builder prepareConnect(RequestContext<M> requestContext, Builder requestBuilder) {
		// TODO Auto-generated method stub
		return null;
	}

	private <M extends IModel>  Builder prepareTrace(RequestContext<M> requestContext, Builder requestBuilder) {
		// TODO Auto-generated method stub
		return null;
	}

	private <M extends IModel>  Builder prepareOptions(RequestContext<M> requestContext, Builder requestBuilder) {
		// TODO Auto-generated method stub
		return null;
	}

	

	/**
	 * 添加header，多次添加同一个key不会覆盖,同一个key可以对应多个值
	 * @param requestContext
	 * @param requestBuilder
	 */
	private <M extends IModel> void addHeaders(RequestContext<M> requestContext, Request.Builder requestBuilder) {
		ListMultimap<String, String> multiHeaders = requestContext.getHeaders();
		Set<String> multiHeaderKeys = multiHeaders.keySet();
		for(String key : multiHeaderKeys){
			List<String> headerValueList = multiHeaders.get(key);
			for(String value : headerValueList){
				requestBuilder.addHeader(key, value);
			}
		}
	}

	/**
	 * 添加header，多次添加同一个key会覆盖
	 * 
	 * @param requestContext
	 * @param builder
	 * @return
	 */
	private <M extends IModel> Request.Builder setHeader(RequestContext<M> requestContext,Request.Builder builder){
		Map<String,String> header = requestContext.getHeader();
		Set<String> headerKeys = header.keySet();
		for(String key : headerKeys){
			builder.header(key, header.get(key));
		}
		return builder;
	}

	@Override
	public <M extends IModel>void sendRequest(RequestContext<M> requestContext) {
		Request request = prepareRequestBuilder(requestContext).build();
		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(response.isSuccessful()){
					try{
						String jsonString = response.body().string();
						System.out.println("jsonString = "+ jsonString);
						IHttpCallback<M> callback = requestContext.getCallback();
						if(callback != null){
							callback.onSuccess(jsonService.fromJson(jsonString, requestContext.getResultClass()));	
						}
					}catch (Exception e) {
						e.printStackTrace();
						//FIXME 不同的Exception可以用来区分不用的错误逻辑，不如：认证失败，协议错误，等
						IOException ioe = new IOException();
						onFailure(call, ioe);
					}
				}
			}
			
			@Override
			public void onFailure(Call call, IOException e) {
				//FIXME 这里可以根据实际中需要，不断完善错误类型。
				IHttpCallback<M> callback = requestContext.getCallback();
				if(callback != null){
					requestContext.getCallback().onFailure(new ErrorResult(111, "根据实际需要，扩充错误类型", "例如：json语法错误，authority错误，SSL证书错误等等"));	
				}
			}
			
		});
	}
}
