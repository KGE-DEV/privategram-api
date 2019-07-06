package com.garrettestrin.PrivateGram.data;

import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.garrettestrin.PrivateGram.api.ApiObjects.User;

public interface UserDao extends SqlObject {

    @SqlQuery("SELECT * "
        +   "FROM users "
        +   "WHERE id = :id")
    @RegisterBeanMapper(User.class)
    User getUser(@Bind("id") long id);
}
