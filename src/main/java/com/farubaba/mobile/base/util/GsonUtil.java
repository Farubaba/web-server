package com.farubaba.mobile.base.util;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class GsonUtil {
	
	/**
	 * 
	 * @param typeIndicator
	 * @return
	 */
	public static <T> Type getListTypeToken(T typeIndicator){
		return new TypeToken<List<T>>(){}.getType();
	}

}
