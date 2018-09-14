# play_demo
---
### 名称说明
```
appversion  app版本号,初始为0.0.1
apptype     app类型 用户端:101 管理端:102 
osversion   运行系统版本(WEB端为浏览器版本)
clienttype  客户端类型 WEB:100 IOS:101 ANDROID:102
devicetoken 设备唯一标识
randomseed  随机种子,客户端每次POST请求需生成一个对应的16位随机字符串,后台保留10分钟,用于判断重复提交
accesstoken 用户凭证,登录成功后后台生成返回给客户端
```
> 以上参数均带在http请求的header内,key均小写

### 统一返回格式说明
```
{
"status":"succ",//succ成功,fail异常
"code":20000,//状态码
"message":"请求成功",//状态码对应提示语
"systemTime":1500000000000,//服务器当前时间戳
"data":{}//业务数据块
}
```
>+ 内部开发环境地址 `http(s)://dev.api.demo.com` 
>+ 对外测试环境地址 `http(s)://test.api.demo.com`
>+ 正式生产环境地址 `http(s)://(www.)api.demo.com`
>+ 请求协议为`http`,编码`utf8`,类型`post`<br/>
>+ 时间类型全部使用`Long`型(既`1970-01-01 00:00:00`到现在的时间戳)<br/>
>+ 布尔类型全部使用`int`型(`0`表示`false``1`表示`true`)<br/>
>+ 价格金额全部使用`int`型(单位精确到分)<br/>
>+ 二维码统一格式{"type":101,"data":"二维码数据","addition":"附加数据"}

### 接口模块

#### [管理端](/readme/admin) 

#### [用户WEB端](/readme/user)

#### [用户移动端](/readme/userapp)

#### [用户h5端](/readme/userh5)