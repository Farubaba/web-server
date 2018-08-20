package com.farubaba.mobile.server.action;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.farubaba.mobile.server.model.User;
import com.farubaba.mobile.server.service.DataService;
import com.farubaba.mobile.server.service.DataServiceImpl;
import com.opensymphony.xwork2.ActionSupport;

public class OkHttpAction extends ActionSupport {
	
	private DataService dataService = new DataServiceImpl();
	private List<User> users; 
	private String userListJsonString;
	
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
	
}
