package com.farubaba.mobile.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.farubaba.mobile.base.http.HttpManager;
import com.farubaba.mobile.base.http.OkHttpAdapter;
import com.farubaba.mobile.base.http.protocol.IHttpCallback;
import com.farubaba.mobile.base.http.protocol.RequestContext;
import com.farubaba.mobile.base.util.ConcurrentUtil;
import com.farubaba.mobile.server.dao.ListUser;
import com.farubaba.mobile.server.dao.ListUser2;
import com.farubaba.mobile.server.model.ErrorResult;

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
		
		httpManager.sendRequest(requestContext);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
