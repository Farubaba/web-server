package com.farubaba.mobile.server.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.farubaba.mobile.server.model.Book;
import com.farubaba.mobile.server.model.User;
import com.farubaba.mobile.server.service.ConfigService;
import com.farubaba.mobile.server.service.SysConfigService;
import com.opensymphony.xwork2.ActionSupport;

//@ParentPackage("dataserver")
//@Namespace("/api/sys/config") 
@ParentPackage("dataserver")  
@Namespace("/api/sys/config")  
@Results({  
    @Result(name = "json",type="json", params={"root","user"})  
}) 
public class ConfigurationAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 989849337948817719L;
	private ConfigService configService = new SysConfigService();
	private User user;

	private String name;
	
	@Action(value="v1")  
	public String configv1(){
		user = configService.sysConfig("v1");
		return "json";
	}

	public User getUser() {
		return user;
	}

	public String getName() {
		name = "testname";
		return name;
	}

	
	
}
