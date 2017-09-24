#### 主要是httpclient各种交互场景的案例

* HttpServer

里面封装了一个工具类方法，发post请求（包含param参数、cookie）

* ClientWithResponseHandler

通过ResponseHandler来处理HTTP响应

* ClientExecuteProxy

通过代理的方式发送一个请求，并对reponse解析