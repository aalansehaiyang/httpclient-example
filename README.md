### 主要是httpclient各种交互场景的案例

* HttpServer

里面封装了一个工具类方法，发post请求（包含param参数、cookie）

* ClientWithResponseHandler

通过ResponseHandler来处理HTTP响应

* ClientExecuteProxy

通过代理的方式发送一个请求，并对response解析

* ClientConnectionRelease

如何正确关闭http请求的连接

* ClientConfiguration

通过各种定制化配置参数来定制http请求，可以应对各种复杂场景

* ClientAuthentication

目标站点需要用户授权验证

* ClientProxyAuthentication

代理+授权认证

* ClientCustomContext

使用本地context（比如手动配置cookie）发请求

* ClientCustomSSL

通过SSL context 发请求