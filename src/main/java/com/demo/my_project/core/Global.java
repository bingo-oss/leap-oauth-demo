package com.demo.my_project.core;

import leap.core.AppContext;
import leap.core.annotation.Inject;
import leap.oauth2.rs.OAuth2ResServerConfigurator;
import leap.web.App;
import leap.web.security.SecurityConfigurator;
import leap.web.security.user.UserDetails;
import leap.web.security.user.UserStore;
import com.demo.my_project.core.entity.User;

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

        // SSO服务器配置
        rsc.enable()
            .useRemoteAuthorizationServer()
            .setRemoteTokenInfoEndpointUrl(getProperty(OAUTH_SERVER_URL) + "/oauth2/tokeninfo")
            .setResourceServerId(getProperty(OAUTH_CLIENT_ID))
            .setResourceServerSecret(getProperty(OAUTH_SECRET));

        // 指定SSO用户查询
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
