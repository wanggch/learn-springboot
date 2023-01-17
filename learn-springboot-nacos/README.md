# Spring Boot | Spring Boot整合Nacos实现配置信息动态变更

我们在开发spring boot web项目时，项目的配置信息（这里以数据源为例）通常会放在`application.properties`文件中。

如果你非得给我争：“也可以放在`application.yml`文件中啊”，我只能回你四个字：“你个杠精”。

当项目配置信息需要变动（比如数据库连接用户名、密码需要修改）时，我们的操作步骤一般是：

1. 修改`application.properties`文件中的项目配置信息；
2. 为了使修改后的配置信息生效，我们需要重启项目。

如果我们使用微服务架构，我们的项目通常是由多个子项目组成。如果这些子项目的配置信息都需要修改，那我们要逐个项目修改了。

逐个修改！看到“逐个”我想你已经不乐意了。

“我就想改一个”

好，我们就改一个。

Nacos配置管理官方文档说明：
> Nacos 真正将配置从应用中剥离出来，统一管理，优雅的解决了配置的动态变更、持久化、运维成本等问题。

通俗地讲，Nacos的配置管理功能的作用就是当需要修改项目配置信息时，我们只需要修改一处，所有的项目就都生效了。这功能绝对Nice！

下面我们就写个示例程序讲述spring cloud项目整合nacos实现配置管理功能。

**首先，我们的一个先决条件，我们需要安装好Nacos。**

本篇文章是通过Docker安装的Nacos。

1. 拉取镜像
```bash
docker pull nacos/nacos-server
```

2. 启动容器
```bash
docker run --name nacos -e MODE=standalone -p 8848:8848 -d nacos/nacos-server
```

`standalone`代表着单机模式运行。

3. 登录

访问地址：`http://127.0.0.1:8848/nacos`  
用户名：`nacos`  
密码：`nacos`

4. 在`配置管理` --> `配置列表`页面创建一个新的配置，配置信息如下：

```
DataId：learn-springboot-nacos-dev.yaml
Group：dev
配置格式：YAML
```

DataId的格式：`${prefix}-${spring.profile.active}.${file-extension}`。

`${prefix}`的值默认为`spring.application.name`，也可以通过配置项`spring.cloud.nacos.config.prefix`配置。

`${spring.profile.active}`为当前环境对应的`profile`。当`spring.profile.active`值为空时，对应的连接符`-`也就没有了，即DataId的格式就成了`${prefix}.${file-extension}`。

`${file-extension}`各配置文件的扩展名，可以通过配置项`spring.cloud.nacos.config.file-extension`配置。

`Group`也就是分组。我所接触的项目大体是通过环境进行划分的。比如：`dev`、`uat`、`test`、`prod`。

配置内容输入如下内容：

```yaml
name: how are you?
```

**二、创建一个Maven项目**

1. 添加如下依赖：
```xml
<dependency>
   <groupId>com.alibaba.cloud</groupId>
   <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
   <version>2021.1</version>
</dependency>
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

2. 创建Spring Boot项目启动类

就是一个非常标准、非常普通的Spring Boot项目启动类。代码我不想贴了。相信你能理解的。

3. 创建项目配置文件 —— bootstrap.yml
```yaml
server:
  port: 9944
spring:
  application:
    name: learn-springboot-nacos
  cloud:
    nacos:
      config:
        file-extension: yaml
        group: ${spring.profiles.active}
        prefix: ${spring.application.name}
        server-addr: 127.0.0.1:8848
  profiles:
    active: dev
```

这里配置的nacos配置信息与在Nacos管理后台新增的配置信息要对应的。

4. 创建一个测试控制器

```java
@RefreshScope
@RestController
public class HelloController {
    @Value("${name}")
    private String name;
    @GetMapping
    public String index() {
        return name;
    }
}
```

很简单的控制器，就是注入下在Nacos中增加的配置内容中的属性，然后直接返回。

一切准备就绪，启动项目。不出意外的话这里要出问题了。

启动项目，控制台输出如下错误信息：

```bash
Injection of autowired dependencies failed; nested exception is java.lang.IllegalArgumentException: Could not resolve placeholder 'name' in value "${name}"
```

Why?

为什么`@Value`无法绑定配置？

我所有的步骤都是参考官网一步一步来的啊，怎么会出错了呢？

正当我毫无头绪，无比困惑，准备放弃时，最后一次Google却找到了处理办法。

这是我Google的关键字：`spring cloud alibaba nacos @Value 无法绑定配置`。

这是在Github已经关闭的一个问题：`https://github.com/alibaba/spring-cloud-alibaba/issues/2048`。

解决办法是添加如下依赖：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
    <version>3.1.3</version>
</dependency>
```

添加完依赖后，我们再次启动项目。这次没有意外了。大功告成。

我们访问下`http://localhost:9944/`看到显示的是`how are you?`。

然后我们去Nacos配置管理中修改配置，修改后的配置内容如下：

```yaml
name: I'm fine.
```

刷新页面（`http://localhost:9944/`），看到的是不是就是`I'm fine.`了。

> 码字不易，如果觉得写得还凑合，还请帮忙点个赞。

