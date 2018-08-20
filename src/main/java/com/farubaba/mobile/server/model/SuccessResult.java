package com.farubaba.mobile.server.model;

public class SuccessResult<T> implements Result {
	
	private String apiVersion;
	private T data;
	public String getApiVersion() {
		return apiVersion;
	}
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
}
