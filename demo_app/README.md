# play_demo
---
### 名称说明
```
appversion  app版本号,初始为0.0.1
apptype     app类型 用户端:101 管理端:102 
osversion   运行系统版本(WEB端为浏览器版本)
clienttype  客户端类型 WEB:100 IOS:101 ANDROID:102
devicetoken 设备唯一标识
randomseed  随机种子,客户端每次POST请求生成一个对应的16位随机字符串,后台保留10分钟,用于判断重复提交
accesstoken 用户凭证,登录成功后后台生成返回给客户端
```
>+ 以上参数均带在请求的`header`内,`key`均小写

### 返回格式说明
```
{
"status":"succ",//状态 成功:succ 异常:fail
"code":20000,//状态码
"message":"请求成功",//状态码对应提示语
"validation":{},//校验数据块
"data":{},//业务数据块
"systemTime":1500000000000//服务器当前时间戳
}
```

### 校验逻辑说明
```
validation:
{
"title":"提示标题",
"content":"提示内容",
"image":"提示图片",
"cancelText":"取消按钮文案",
"cancelType":101,//取消按钮行为 关闭弹窗:101 重新请求:102 链接跳转:103 返回上级:104
"submitText":"确认按钮文案",
"submitType":101,//确认按钮行为 关闭弹窗:101 重新请求:102 链接跳转:103 返回上级:104
"type":101//弹窗类型 Dialog:101 Toast:102
}
```
>+ 异常时返回`validation`信息,用于提醒及后续行为
>+ 接口参数列表标注`validation`时,此接口需经过校验确认,校验时`validation`不传,校验返回后重新发起请求,`validation`传`1`

### 全局统一规定
```
内部开发环境地址 http(s)://dev.api.demo.com
对外测试环境地址 http(s)://test.api.demo.com
提审预发环境地址 http(s)://pre.api.demo.com
正式生产环境地址 http(s)://(www.)api.demo.com
请求协议为http,编码utf8,类型post
时间类型全部使用Long型(既1970-01-01 00:00:00到现在的时间戳)
布尔类型全部使用int型(0表示false 1表示true)
价格金额全部使用int型(单位精确到分)
二维码统一格式{"type":101,"data":"二维码数据","addition":"附加数据"}
```

### 接口模块

#### [管理端](/readme/admin) 

#### [用户WEB端](/readme/user)

#### [用户移动端](/readme/userapp)

#### [用户h5端](/readme/userh5)