
## 一、Undertow简介

> Undertow is a flexible performant web server written in java, providing both blocking and non-blocking API’s based on NIO.

`Undertow`是一个灵活的、高性能的Web服务器，提供了基于NIO的阻塞和非阻塞API。

## 二、为什么选择Undertow？

1、**HTTP/2 Support**

`Undertow`支持HTTP/2，开箱即用，无需重写引导类路径。

2、**支持HTTP升级**

支持HTTP升级，允许在HTTP端口上复用多个协议。

3、**支持Web Sockets**

`Undertow`提供对Web Sockets的全面支持，包括JSR-356支持。

4、**支持Servlet 4.0**

`Undertow`提供对Servlet 4.0的支持，包括对嵌入式servlet的支持。 也可以在同一部署中混合使用Servlet和原生Undertow非阻塞处理程序。

5、**可嵌入式**

`Undertow`可以嵌入到应用程序中，也可以通过几行代码独立运行。

6、**灵活**

`Undertow`服务器是通过将处理程序链接在一起来配置的。 可以按需配置模块，无须加载多余的模块。

## 三、使用Undertow

因为Spring boot使用`Tomcat`作为默认的Web服务器，所以要使用`Undertow`作为内置Web服务器，需要先移除`Tomcat`。

移除`Tomcat`：

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
   <exclusions>
      <exclusion>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-tomcat</artifactId>
      </exclusion>
   </exclusions>
</dependency>
```

添加`Undertow`依赖：

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-undertow</artifactId>
   <version>2.4.1</version>
</dependency>
```

配置`Undertow`：

```yaml
server:
  undertow:
    # 每块buffer的空间大小,越小的空间被利用越充分，不要设置太大，以免影响其他应用，合适即可
    buffer-size: 16364
    # 每个区分配的buffer数量 , 所以pool的大小是buffer-size * buffers-per-region
    #buffers-per-region: 1024
    # 是否分配的直接内存(NIO直接分配的堆外内存)
    direct-buffers: true
    threads:
      # 设置IO线程数, 它主要执行非阻塞的任务
      io: 8
      # 阻塞任务线程池, 当执行类似servlet请求阻塞操作, undertow会从这个线程池中取得线程,它的值设置取决于系统的负载
      worker: 256
    accesslog:
      # 是否开启访问日志
      enabled: false
```

## 后记

相比于`Tomcat`，`Undertow`还是比较小众，资料较少。如非必要，不建议换。