package com.farubaba.mobile.base.http.protocol;

public interface HttpCallback<R extends IModel, Req extends IRequestContext, Res extends IResponseContext> extends IHttpCallback {
	public void onSuccess(Req request, Res response, R result);
	public void onFailure(Req request, Res response, R result);
}
