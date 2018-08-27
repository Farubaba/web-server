package com.farubaba.mobile.base.http.model;

public class ObjectErrorModel extends StateAwareModel {
	
	public ErrorDetectModel error;

	public ErrorDetectModel getError() {
		return error;
	}

	public void setError(ErrorDetectModel error) {
		this.error = error;
	}
	
}
