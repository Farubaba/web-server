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

[Struts编写接口API，返回JSON数据]:https://blog.csdn.net/xiaoxiaoxianxian/article/details/40322473
[Struts2 Convention Plugin]:http://struts.apache.org/plugins/convention/#setup