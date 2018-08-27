package com.farubaba.mobile.server.model;

import com.farubaba.mobile.base.http.model.IModel;

public class ErrorResult implements IModel{
	/**
	 * 错误代码
	 */
	private int code;
	/**
	 * 错误代码的业务解释
	 */
	private String message;
	/**
	 * 错误代码对应的前端显示内容
	 */
	private String display;
	
	public ErrorResult(int code, String message, String display) {
		super();
		this.code = code;
		this.message = message;
		this.display = display;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
}
