package com.demo.my_project.api;

import com.demo.my_project.core.entity.User;
import leap.web.annotation.Path;
import leap.web.annotation.PathParam;
import leap.web.annotation.http.DELETE;
import leap.web.annotation.http.GET;
import leap.web.annotation.http.PATCH;
import leap.web.annotation.http.POST;
import leap.web.api.mvc.ApiResponse;
import leap.web.api.mvc.ModelController;
import leap.web.api.mvc.params.DeleteOptions;
import leap.web.api.mvc.params.Partial;
import leap.web.api.mvc.params.QueryOptions;
import leap.web.api.mvc.params.QueryOptionsBase;

import java.util.List;

/**
 * leap示例, 可删除.
 * Created by Leap maven archetype.
 */
@Path("user")
public class UserController extends ModelController<User> {

    @GET
    public ApiResponse<List<User>> query(QueryOptions options) {
        return queryList(options);
    }

    @GET("/{id}")
    public ApiResponse<User> find(@PathParam Object id, QueryOptionsBase options) {
        return get(id, options);
    }

    @POST
    public ApiResponse create(Partial<User> partial) {
        return createAndReturn(partial);
    }

    @DELETE("/{id}")
    public ApiResponse delete(@PathParam Object id, DeleteOptions options) {
        return delete(id, options);
    }

    @PATCH("/{id}")
    public ApiResponse update(@PathParam Object id, Partial<User> partial) {
        return updatePartial(id, partial);
    }
}
