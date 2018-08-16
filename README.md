---
title: Struts2 使用struts2-json-plugin返回JSON数据。
---

### 参考资料

[Struts2 Convention Plugin]

[Struts编写接口API，返回JSON数据]

```xml
<result type="json">  
    <!-- 这里指定将被Struts2序列化的属性，该属性在action中必须有对应的getter方法 -->  
    <!-- 默认将会序列所有有返回值的getter方法的值，而无论该方法是否有对应属性 -->  
    <!-- 使用root去除dataMap在json中生成变量-->  
    <param name="root">dataMap</param>  
    <!-- 指定是否序列化空的属性 -->  
    <param name="excludeNullProperties">true</param>  
    <!-- 这里指定将序列化dataMap中的那些属性 -->  
    <param name="includeProperties">user.*</param>  
    <!-- 这里指定将要从dataMap中排除那些属性，这些排除的属性将不被序列化，一般不与上边的参数配置同时出现 -->  
    <param name="excludeProperties">flag</param>  
</result> 
```

### 加入struts2-convention-plugin 和 struts2-json-plugin之后的问题

1. 在struts.xml中增加配置：\<constant name="struts.action.extension" value="" /> , 目标是访问: 不带.action后缀的url也能被拦截，处理之后返回json数据。例如：http://localhost:8080/mobile-server/api/sys/config/v1 返回系统配置json数据。但是，这样却导致，所有以.html、.htm、.jsp、.xml等结尾的url也将被拦截，并且在用这些url去寻找与之匹配的action时，发生错误，不能找到对应的action。

```	
	如何配置才能让api接口不带.action后缀就可以访问到，而普通action请求带.action，同时又不会拦截类似.html, .jsp 结尾的URL，保证他们能正常被访问到，而不是去找他们对应的action（必然是找不到的）
```
因为需要直接访问的html、jsp文件，都不会放在WEB-INF下面，否则只能通过action跳转。所以，我们可以把这些页面一一对应配置上action。例如：

	```
		<action name="HelloWorld.jsp">
            <result>/HelloWorld.jsp</result>
        </action>
        <action name="model.jsp">
            <result>/model.jsp</result>
        </action>
        <action name="road-map.jsp">
            <result>/road-map.jsp</result>
        </action>
	```

2. Struts.xml中<action name="login">的配置莫名其妙的就找不到对应的action了，这个问题，目前采用更改action名称，避免使用login来处理。后续再研究为什么。


[Struts编写接口API，返回JSON数据]:https://blog.csdn.net/xiaoxiaoxianxian/article/details/40322473
[Struts2 Convention Plugin]:http://struts.apache.org/plugins/convention/#setup