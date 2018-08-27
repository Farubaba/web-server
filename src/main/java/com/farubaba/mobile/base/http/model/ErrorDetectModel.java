package com.farubaba.mobile.base.http.model;

/**
 * 这些字段需要和接口定义中的属性匹配
 * @author violet
 *
 */
public abstract class ErrorDetectModel implements IModel{
	private int code;
	private String message;
	private String display;
	private String type;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
