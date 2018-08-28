package com.farubaba.mobile.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.farubaba.mobile.base.http.HttpManager;
import com.farubaba.mobile.base.http.OkHttpAdapter;
import com.farubaba.mobile.base.http.body.StringRequestBody;
import com.farubaba.mobile.base.http.model.ObjectErrorModel;
import com.farubaba.mobile.base.http.protocol.HttpMethod;
import com.farubaba.mobile.base.http.protocol.IHttpCallback;
import com.farubaba.mobile.base.http.protocol.MimeType;
import com.farubaba.mobile.base.http.protocol.RequestContext;
import com.farubaba.mobile.base.http.protocol.RequestHandler;
import com.farubaba.mobile.base.util.ConcurrentUtil;
import com.farubaba.mobile.server.dao.ListUser;
import com.farubaba.mobile.server.dao.ListUser2;
import com.farubaba.mobile.server.model.ErrorResult;
import com.farubaba.mobile.server.model.SuccessResult;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Struts2-json-plugin 继承属性无法转化到JSON
 * MultipartBody 需要验证每一个part对应的MediaType是否正确设置
 * 
 * @author violet
 *
 */
public class OkHttpManagerDataCenter {
	
	public static final String API_CONTEXT= "http://127.0.0.1:8080/mobile-server/";
	
	String domain = "http://127.0.0.1:8080/mobile-server";
	String patternUrl = "api/user/v2?name=%1$s&name=%2$s";
	String fullUrl = "http://127.0.0.1:8080/mobile-server/api/user/getUsersApi?name=%1$s&name=%2$s";
	
	@Test
	public void okHttpGetTest(){
	
		OkHttpAdapter manager = new OkHttpAdapter();
		Map<String,String> querys = new HashMap<String, String>();
		querys.put("name", "namevalue-from-map");
		querys.put("pwd", "123");
		RequestContext<ListUser> requestContext = new RequestContext<ListUser>()
				.setDomain(domain)
				.setUrl(String.format(patternUrl, "valueOfName1","valueOfName2"))
				.setQuerys(querys);
				
		manager.sendRequest(requestContext);
	}
	
	
	@Test
	public void okHttpGetFullUrlTest(){
	
		OkHttpAdapter manager = new OkHttpAdapter();
		Map<String,String> querys = new HashMap<String, String>();
		querys.put("name", "namevalue-from-map2");
		querys.put("pwd", "123456");
		
		RequestContext<ListUser> requestContext = new RequestContext<ListUser>();
		requestContext.setDomain(domain)
				.setUrl(String.format(fullUrl, "valueOfName11","valueOfName22"))
				.setQuerys(querys);
				
		manager.sendRequest(requestContext);
	}
	
	@Test
	public void asyncOkHttpGetTest(){
	
		System.out.println("发送请求的线程ID ："+Thread.currentThread().getId());
		
		HttpManager httpManager = HttpManager.getInstance();
		Map<String,String> querys = new HashMap<String, String>();
		querys.put("name", "namevalue-from-map2-asyc");
		querys.put("pwd", "123456");
		
		IHttpCallback<ListUser2> callback = new IHttpCallback<ListUser2>() {
			@Override
			public void onSuccess(ListUser2 result) {
					String name = result.getData().get(0).getUsername();
					System.out.println("接收response的线程ID："+Thread.currentThread().getId()+"  成功获得userName = "+name);
							
			}

			@Override
			public void onFailure(ErrorResult result) {
				// TODO Auto-generated method stub
				System.out.println("接收到的错误：  " + result.getMessage());
				
			}
		};
		RequestContext<ListUser2> requestContext = new RequestContext<ListUser2>()
				.setDomain(domain)
				.setUrl(String.format(fullUrl, "valueOfName11","valueOfName22"))
				.setQuerys(querys)
				.setCallback(callback)
				.setResultClass(ListUser2.class);
	
				
		httpManager.sendRequest(requestContext);
	}
	
	@Test
	public void asyncOkHttpGetTest2(){
	
		System.out.println("发送请求的线程ID ："+Thread.currentThread().getId());
		
		HttpManager httpManager = HttpManager.getInstance();
		
		Map<String,String> querys = new HashMap<String, String>();
		querys.put("name", "namevalue-from-map2-asyc-test2");
		querys.put("pwd", "1234567890");
		
		final CountDownLatch countDownLatch = ConcurrentUtil.newSingleStepCountDownLatch();
		IHttpCallback<ListUser2> callback = new IHttpCallback<ListUser2>() {
			@Override
			public void onSuccess(ListUser2 result) {
					String name = result.getData().get(0).getUsername();
					System.out.println("接收response的线程ID："+Thread.currentThread().getId()+"  成功获得userName = "+name);
					countDownLatch.countDown();	
			}

			@Override
			public void onFailure(ErrorResult result) {
				// TODO Auto-generated method stub
				System.out.println("接收到的错误：  " + result.getMessage());
				countDownLatch.countDown();	
				
			}
		};
		
		RequestContext<ListUser2> requestContext = new RequestContext<ListUser2>()
				//.setDomain(domain)
				.setUrl(String.format(fullUrl, "valueOfName11","valueOfName22"))
				.setQuerys(querys)
				.setCallback(callback)
				.setResultClass(ListUser2.class);
		
		RequestHandler handler = httpManager.sendRequest(requestContext);
		//you can cancel request like below:
		handler.cancelRequest();
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void httpManagerPostString(){
		final CountDownLatch countDownLatch = ConcurrentUtil.newSingleStepCountDownLatch();
		String postStringUrlWithUrlParamter = "http://127.0.0.1:8080/mobile-server/api/bussiness/postString?name=%1$s&pwd=%2$s";
		String bodyContent = "hello body, this is the message post by a beautiful girl, who lives on the other size of the earth, don't you exciting?";
		HttpManager manager = HttpManager.getInstance();
		RequestContext<ObjectErrorModel> requestContext = new RequestContext<ObjectErrorModel>()
				.setUrl(String.format(postStringUrlWithUrlParamter, "lzg", "123"))
				.setMethod(HttpMethod.POST)
				.setRequestBody(new StringRequestBody()
						.setMimeType(MimeType.TEXT_X_MARKDOWN)
						.setBodyContent(bodyContent))
				.setResultClass(ObjectErrorModel.class)
				.setCallback(new IHttpCallback<ObjectErrorModel>() {
					@Override
					public void onSuccess(ObjectErrorModel result) {
						System.out.println("正确返回 : result.display = " + result.getDisplay() );
						System.out.println("正确返回 : result.getError().display = " + result.getError().getDisplay() );
						countDownLatch.countDown();
					}

					@Override
					public void onFailure(ErrorResult result) {
						System.out.println("错误返回");
						countDownLatch.countDown();
					}
				});
		manager.sendRequest(requestContext);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
