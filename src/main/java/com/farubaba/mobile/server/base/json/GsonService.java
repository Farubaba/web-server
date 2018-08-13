package com.farubaba.mobile.server.base.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonService implements JsonService {

	public static final Gson gson = new GsonBuilder().create();
	private static GsonService instance = new GsonService();
	
	public static GsonService getInstance(){
		return instance;
	}
	
	@Override
	public <E> String toJsonString(E e) {
		return gson.toJson(e);
	}

	@Override
	public <T> T fromJson(String jsonString, Class<T> classOfT) {
		return gson.fromJson(jsonString, classOfT);
	}
}
