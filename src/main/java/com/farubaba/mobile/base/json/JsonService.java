package com.farubaba.mobile.base.json;

public interface JsonService{
	
	public <T> T fromJson(String jsonString, Class<T> classOfT);
	
	public <E> String toJsonString(E e);
}
