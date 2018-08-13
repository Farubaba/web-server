package com.farubaba.mobile.server.service;

import com.farubaba.mobile.server.action.LoginAction;
import com.farubaba.mobile.server.base.json.GsonService;
import com.farubaba.mobile.server.base.json.JsonFactory;
import com.farubaba.mobile.server.base.json.JsonService;
import com.farubaba.mobile.server.dao.UserDao;
import com.farubaba.mobile.server.dao.UserLoginDao;
import com.farubaba.mobile.server.model.ErrorResult;
import com.farubaba.mobile.server.model.User;

public class UserLoginService implements LoginService{
	
	protected JsonService jsonService = JsonFactory.getJsonService();
	protected UserDao userDao = new UserLoginDao();

	@Override
	public String login(String userName, String password) {
		User user = userDao.login(userName, password);
		//登陆逻辑判断，并返回给客户端不同的response内容
		if(user == null || !user.isLogin()){
			ErrorResult  error = new ErrorResult(1404, "USER_NOT_FOUNT", "用户不存在！");
			return jsonService.toJsonString(error);
		}
		return jsonService.toJsonString(user);
	}

	@Override
	public User userLogin(String userName, String password) {
		User user = userDao.login(userName, password);
		return user;
	}

}
