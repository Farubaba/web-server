package com.farubaba.mobile.server.action;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;

import com.farubaba.mobile.base.util.IOUtil;
import com.farubaba.mobile.server.model.User;
import com.farubaba.mobile.server.service.DataService;
import com.farubaba.mobile.server.service.DataServiceImpl;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.opensymphony.xwork2.ActionSupport;

/**
 * /api/user
 */
public class OkHttpAction extends ActionSupport {
	
	private DataService dataService = new DataServiceImpl();
	private List<User> users; 
	private String userListJsonString;
	private ListMultimap<String,String> headerMap = MultimapBuilder.hashKeys().arrayListValues().build();
	private String jsonString;
	
	@Action("v1")
	public String getUserList(){
		try {
			Method method = this.getClass().getMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
			String apiVersion = method.getAnnotation(Action.class).value();
			users = dataService.getUsers(apiVersion); 
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	@Action("v2")
	public String getUserListApi(){
		Method method;
		try {
			method = this.getClass().getMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
			String apiVersion = method.getAnnotation(Action.class).value();
			users = dataService.getUsers(apiVersion); 
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return "json";
	}
	
	
	@Action("v3")
	public String getUserListStringApi(){
		Method method;
		try {
			method = this.getClass().getMethod(Thread.currentThread().getStackTrace()[1].getMethodName());
			String apiVersion = method.getAnnotation(Action.class).value();
			userListJsonString = dataService.getUserListJson(apiVersion);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return "json";
	}
	

	@Action("v4")
	public String accessHeader(){
		HttpServletRequest request = ServletActionContext.getRequest();
		Enumeration<String> headers = request.getHeaderNames();
		StringBuilder sb = new StringBuilder();
		while(headers.hasMoreElements()){
			String headerName = headers.nextElement();
			Enumeration<String> headerValues = request.getHeaders(headerName);
			while(headerValues.hasMoreElements()){
				String headerValue = headerValues.nextElement();
				sb.append(headerName + " = " + headerValue).append("\r\n");
				headerMap.put(headerName, headerValue);
			}
		}
		
		ServletActionContext.getResponse().addHeader("cache", "server cache control");
		
		System.out.println("服务器接收到的Header为：");
		System.out.println(sb.toString());
		Set<String> keys = headerMap.keySet();
		System.out.println("放入setMultiMap中的Header为：");
		for(String key : keys){
			System.out.println(key + " = " + headerMap.get(key));
		}
		return "json";
	}
	
	@Action("post_plain_text")
	public String postPlainText(){
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			String readFromClient = IOUtil.readIntoMemery(request.getInputStream(), -1);
			ServletActionContext.getResponse().getOutputStream().write(readFromClient.getBytes());
			ServletActionContext.getResponse().setContentLength(readFromClient.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "json";
	}
	
	@Action("post_stream")
	public String postPStream(){
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			String readFromClient = IOUtil.readIntoMemery(request.getInputStream(), -1);
			ServletActionContext.getResponse().getOutputStream().write(readFromClient.getBytes());
			ServletActionContext.getResponse().setContentLength(readFromClient.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "json";
	}
	
	@Action("post_file")
	public String postFile(){
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			String readFromClient = IOUtil.readIntoMemery(request.getInputStream(), -1);
			System.out.println("服务器接收到的post_file内容："+readFromClient);
			HttpServletResponse response = ServletActionContext.getResponse();
			//错误
			//response.getOutputStream().write(readFromClient.getBytes());
			//response.setContentLength(readFromClient.length());
			
			response.setContentType("text/x-markdown; charset=utf-8");
			response.getWriter().write(readFromClient);
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	//@Action("post_form")
	public String postForm(){
		HttpServletRequest request = ServletActionContext.getRequest();
		jsonString = request.getParameter("name");
		System.out.println("服务器接收到的post_file内容："+jsonString);
		return "json";
	}
	
	public String postMultiPart(){
		StringBuffer sb = new StringBuffer();
		HttpServletRequest request = ServletActionContext.getRequest();
		sb.append(request.getParameter("key"))
		.append("----");
		jsonString = sb.toString();
		return "json";
	}
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public void setUserListJsonString(String userListJsonString) {
		this.userListJsonString = userListJsonString;
	}
	
	public String getUserListJsonString(){
		return this.userListJsonString;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	
	
}
