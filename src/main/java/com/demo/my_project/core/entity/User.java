package com.demo.my_project.core.entity;

import leap.core.doc.annotation.Doc;
import leap.orm.annotation.Column;
import leap.orm.annotation.Id;
import leap.orm.annotation.Table;
import leap.orm.model.Model;
import leap.web.security.user.UserDetails;

/**
 * leap示例, 可删除.
 * Created by Leap maven archetype.
 */
@Table
@Doc("用户")
public class User extends Model implements UserDetails{

    @Id
    private String id;
    @Column
    @Doc("用户名称")
    private String name;

    @Column
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getLoginName() {
        return name;
    }
}
