package com.farubaba.mobile.base.http.protocol;

import com.farubaba.mobile.base.http.model.IModel;
import com.farubaba.mobile.server.model.ErrorResult;

public interface IHttpCallback<M extends IModel> {
	public void onSuccess(M result);
	public void onFailure(ErrorResult result);
}
