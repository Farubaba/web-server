package com.farubaba.mobile.base.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.farubaba.mobile.base.http.body.Body;
import com.farubaba.mobile.base.http.body.FileRequestBody;
import com.farubaba.mobile.base.http.body.FormRequestBody;
import com.farubaba.mobile.base.http.body.HeaderedBody;
import com.farubaba.mobile.base.http.body.MultipartRequestBody;
import com.farubaba.mobile.base.http.body.StreamRequestBody;
import com.farubaba.mobile.base.http.body.StringRequestBody;
import com.farubaba.mobile.base.http.model.IModel;
import com.farubaba.mobile.base.http.protocol.HttpAdapter;
import com.farubaba.mobile.base.http.protocol.HttpMethod;
import com.farubaba.mobile.base.http.protocol.IModelResultCallback;
import com.farubaba.mobile.base.http.protocol.RequestContext;
import com.farubaba.mobile.base.http.protocol.RequestHandler;
import com.farubaba.mobile.base.json.JsonFactory;
import com.farubaba.mobile.base.json.JsonService;
import com.farubaba.mobile.base.util.CloseUtil;
import com.farubaba.mobile.base.util.IOUtil;
import com.farubaba.mobile.server.model.ErrorResult;
import com.google.common.collect.ListMultimap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Part;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

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
		requestBuilder.url(requestContext.getUrl());
		RequestBody body = prepareRequestBodyIfNeed(requestContext, requestBuilder);
		HttpMethod method = requestContext.getMethod();
		switch(method){
			case GET: //完全方法
				return requestBuilder.get();
			case HEAD: //安全方法
				return requestBuilder.head();
			case POST:
				return requestBuilder.post(body);
			case PUT:
				return requestBuilder.put(body);
			case DELETE:
				return requestBuilder.delete(body);
			case PATCH:
				return requestBuilder.patch(body);
			case CONNECT:
			case TRACE:
			case OPTIONS:
			default:
				return requestBuilder.get();
			
		}
	}
	
	private <M extends IModel> RequestBody prepareRequestBodyIfNeed(RequestContext<M> requestContext, Builder requestBuilder) {
		Body body = requestContext.getRequestBody();
		RequestBody requestBody = convertToRequestBody(body);
		return requestBody;
	}
	
	/**
	 * 这里提供了目前支持的RequestBody类型，如果需要添加其他类型，扩展这里即可。
	 * 
	 * @param body
	 * @return
	 */
	private RequestBody convertToRequestBody(Body body){
		RequestBody requestBody = null;
		if(body != null){
			if(body instanceof StringRequestBody){
				StringRequestBody content = (StringRequestBody)body;
				requestBody = prepareStringBody(content);
			}else if(body instanceof StreamRequestBody){
				StreamRequestBody content = (StreamRequestBody)body;
				requestBody = prepareStreamBody(content);
			}else if(body instanceof FileRequestBody){
				FileRequestBody content = (FileRequestBody) body;
				requestBody = prepareFileBody(content);
			}else if(body instanceof FormRequestBody){
				FormRequestBody content = (FormRequestBody) body;
				requestBody = prepareFormBody(content);
			}else if(body instanceof MultipartRequestBody){
				MultipartRequestBody content =  (MultipartRequestBody) body;
				requestBody = prepareMultipartBody(content);
			}
		}
		return requestBody;
	}

	private RequestBody prepareMultipartBody(MultipartRequestBody content) {
		RequestBody body = null;
		MultipartBody.Builder builder = new MultipartBody.Builder();
		//FIXME 多个不同的part应该使用哪一个mimeType
		//builder.setType(type);
		if(content != null){
			List<Body> bodyList = content.getBodyList();
			List<FileRequestBody> fileBodyList = content.getFileBodyList();
			Map<String,String> formKeyValues = content.getFormKeyValues();
			List<HeaderedBody> headersBodyList = content.getHeadersBodyList();
			addFormDataKeyValueParts(builder,formKeyValues);
			addFormDataFileParts(builder, fileBodyList);
			addBodyParts(builder, bodyList);
			addHeadersBodyParts(builder, headersBodyList);
			body = builder.build();
		}
		return body;
	}

	private void addHeadersBodyParts(okhttp3.MultipartBody.Builder builder, List<HeaderedBody> headersBodyList) {
		for(HeaderedBody item : headersBodyList){
			Headers headers = Headers.of(item.getHeaders());
			Body body = item.getBodyContent();
			builder.addPart(headers, convertToRequestBody(body));
		}
	}

	private void addBodyParts(okhttp3.MultipartBody.Builder builder, List<Body> bodyList) {
		for(Body body : bodyList){
			builder.addPart(convertToRequestBody(body));
		}
	}

	private void addFormDataFileParts(okhttp3.MultipartBody.Builder builder, List<FileRequestBody> fileBodyList) {
		for(FileRequestBody item : fileBodyList){
			builder.addFormDataPart(item.getName(), item.getFileName(), convertToRequestBody(item));
		}
	}

	private void addFormDataKeyValueParts(okhttp3.MultipartBody.Builder builder, Map<String, String> formKeyValues) {
		Set<String> keySet = formKeyValues.keySet();
		for(String key : keySet){
			builder.addFormDataPart(key, formKeyValues.get(key));
		}
	}

	private RequestBody prepareFormBody(FormRequestBody content) {
		RequestBody body = null;
		if(content != null){
			Map<String, String> formMap = content.getBodyContent();
			if(formMap != null && !formMap.isEmpty()){
				FormBody.Builder builder = new FormBody.Builder();
				for(String key : formMap.keySet()){
					if(content.isEncoded()){
						builder.addEncoded(key, formMap.get(key));
					}else{
						builder.add(key, formMap.get(key));
					}
				}
				body = builder.build();
			}
		}
		return body;
	}

	private RequestBody prepareFileBody(FileRequestBody content) {
		RequestBody body = null;
		if(content != null){
			body = RequestBody.create(MediaType.parse(content.getMimeType()), content.getBodyContent());
		}
		return body;
	}

	private RequestBody prepareStreamBody(StreamRequestBody content) {
		RequestBody body = null;
		if(content != null){
			body = new RequestBody() {
				@Override
				public void writeTo(BufferedSink sink) throws IOException {
					InputStream in = content.getBodyContent();
					byte[] buffer = new byte[16 * 1024];
					if(in != null){
						int len = 0;
						while(IOUtil.hasMoreContent(len = in.read(buffer))){
							sink.write(buffer, 0, len);
						}	
					}
					CloseUtil.closeIO(in);
				}
				
				@Override
				public MediaType contentType() {
					return MediaType.parse(content.getMimeType());
				}
			};
		}
		return body;
	}

	private RequestBody prepareStringBody(StringRequestBody content) {
		if(content != null){
			return RequestBody.create(MediaType.parse(content.getMimeType()), content.getBodyContent());
		}
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
	public <M extends IModel> RequestHandler sendRequest(RequestContext<M> requestContext) {
		Request request = prepareRequestBuilder(requestContext).build();
		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(response.isSuccessful()){
					try{
						String jsonString = response.body().string();
						System.out.println("jsonString = "+ jsonString);
						IModelResultCallback<M> callback = requestContext.getCallback();
						if(callback != null){
							callback.onSuccess(jsonService.fromJson(jsonString, requestContext.getResultClass()));	
						}
					}catch (Exception e) {
						e.printStackTrace();
						//FIXME 不同的Exception可以用来区分不用的错误逻辑，不如：认证失败，协议错误，等
						IOException ioe = new IOException(e);
						onFailure(call, ioe);
					}
				}
			}
			
			@Override
			public void onFailure(Call call, IOException e) {
				//FIXME 这里可以根据实际中需要，不断完善错误类型。
				IModelResultCallback<M> callback = requestContext.getCallback();
				ErrorResult errorResult = new ErrorResult(-1, "根据实际需要，扩充错误类型,例如：json语法错误，authority错误，SSL证书错误等等", "其他错误，不特殊处理");
				if(callback != null){
					if(call.isCanceled()){
						errorResult.setCode(ErrorResult.CODE_REQUEST_CANCEL)
						.setMessage("客户端取消了request")
						.setDisplay("无特殊处理");
					}
					callback.onFailure(errorResult);	
				}
			}
			
		});
		
		RequestHandler requestHandler = new RequestHandler() {
			@Override
			public void cancelRequest() {
				call.cancel();
			}
		};
		return requestHandler;
	}
}
