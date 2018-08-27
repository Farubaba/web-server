package com.farubaba.mobile.base.http.model;

/**
 *
 * 根据具体的业务api接口中定义的字段来修改成员变量
 *
 */
public class StringErrorModel extends StateAwareModel{
	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}
