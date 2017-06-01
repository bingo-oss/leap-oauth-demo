# leap-oauth-demo
使用 Leap 接入 OAuth 的示例工程。

# OAuth 身份校验接入文档

实际项目中基本上大部分请求都需要做安全校验以确认用户身份。

因此这里我们介绍如何在项目中接入 OAuth 身份校验。

## 准备

在接入之前，我们先确保我们具备了以下条件：

- 已部署并运行了一台 OAuth 身份校验服务器；
- 在 OAuth 身份校验服务器上注册了我们当前应用的信息。

通过以上两步我们可以获取以下配置信息：

1. oauth.server.url：OAuth 身份校验服务器地址；
2. oauth.client.id：当前应用在服务器上的客户端 ID；
3. oauth.secret：当前应用在服务器上的客户端 secret。

## 配置

Leap 对 OAuth 身份校验的配置很简单。

首先我们先把上面获取到的配置信息加进配置文件中。如：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<config xmlns="http://www.leapframework.org/schema/config">
    <base-package>my.project.core</base-package>
    <properties>
        <!-- SSO服务器地址 -->
        <property name="oauth.server.url">http://1.2.3.4:8000/sso</property>
        <!-- 本应用在SSO服务器上的客户端ID -->
        <property name="oauth.client.id">demo_client_id</property>
        <!-- 本应用在SSO服务器上的secret -->
        <property name="oauth.secret">demo_secret</property>
    </properties>
</config>
```


假设我们之前在 config.xml 配置文件中配置了项目的 base-package 是 `my.project.core` ，那么我们可以在这个包下新建一个 Global 类，在该类中配置身份校验。

代码如下：

```java
package my.project.core;

import leap.core.AppContext;
import leap.core.annotation.Inject;
import leap.oauth2.rs.OAuth2ResServerConfigurator;
import leap.web.App;
import leap.web.security.SecurityConfigurator;
import leap.web.security.user.UserDetails;
import leap.web.security.user.UserStore;
import my.project.core.entity.User;

public class Global extends App {

    private static final String OAUTH_SERVER_URL = "oauth.server.url";
    private static final String OAUTH_CLIENT_ID = "oauth.client.id";
    private static final String OAUTH_SECRET = "oauth.secret";

    @Inject
    private OAuth2ResServerConfigurator rsc;
    @Inject
    public SecurityConfigurator sc;

    @Override
    protected void init() throws Throwable {
        sc.enable();
        sc.setAuthenticateAnyRequests(true);

        // 服务器配置
        rsc.enable()
            .useRemoteAuthorizationServer()
            .setRemoteTokenInfoEndpointUrl(getProperty(OAUTH_SERVER_URL) + "/oauth2/tokeninfo")
            .setResourceServerId(getProperty(OAUTH_CLIENT_ID))
            .setResourceServerSecret(getProperty(OAUTH_SECRET));

        // 指定用户查询
        sc.setUserStore(new UserStore() {

            @Override
            public UserDetails loadUserDetailsById(Object userId) {
                User user= User.findOrNull(userId);
                return user;
            }

            @Override
            public UserDetails loadUserDetailsByLoginName(String loginName) {
                User user=  User.findBy("name", loginName);
                return user;
            }
        });
    }

    private String getProperty(String name) {
        return AppContext.config().getProperty(name);
    }
}
```

在以上代码中

```
sc.enable();
sc.setAuthenticateAnyRequests(true);
```

开启了安全校验并设置拦截所有请求。

```
rsc.enable()
    .useRemoteAuthorizationServer()
    .setRemoteTokenInfoEndpointUrl(getProperty(OAUTH_SERVER_URL) + "/oauth2/tokeninfo")
    .setResourceServerId(getProperty(OAUTH_CLIENT_ID))
    .setResourceServerSecret(getProperty(OAUTH_SECRET));
```

指定了 OAuth 身份校验服务器的配置信息。

```
sc.setUserStore();
```

则指定了当通过请求的 AccessToken 去服务器鉴权并且获得了用户 id 等信息时，如何在本应用数据库中获取用户。

这里本应用的用户实体 User 注意需要实现 leap.web.security.user.UserDetails 接口。