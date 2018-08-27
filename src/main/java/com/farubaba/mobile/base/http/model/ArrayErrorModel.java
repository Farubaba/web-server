package com.farubaba.mobile.base.http.model;

import java.util.List;

public class ArrayErrorModel extends StateAwareModel{
	private List<ErrorDetectModel> error;

	public List<ErrorDetectModel> getError() {
		return error;
	}

	public void setError(List<ErrorDetectModel> error) {
		this.error = error;
	}
	
}
