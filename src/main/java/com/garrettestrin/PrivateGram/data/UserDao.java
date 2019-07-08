package com.garrettestrin.PrivateGram.data;

import com.garrettestrin.PrivateGram.api.ApiObjects.User;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UserDao extends SqlObject {

    // TODO: JAVADOC
    @SqlQuery("SELECT * "
        +   "FROM users "
        +   "WHERE id = :id")
    @RegisterBeanMapper(User.class)
    User getUserById(@Bind("id") long id);

    // TODO: JAVADOC
    @SqlQuery("SELECT id "
            +   "FROM users "
            +   "WHERE email = :email")
    @RegisterBeanMapper(User.class)
    Long getUserIdByEmail(@Bind("email") String email);

    // TODO: JAVADOC
    @SqlUpdate("INSERT INTO "
            + "users (email, first_name, last_name, password) "
            + "VALUES (:email, :first_name, :last_name, :password)")
    boolean registerUser(@Bind("email") String email,
                                 @Bind("first_name") String first_name,
                                 @Bind("last_name") String last_name,
                                 @Bind("password") String password);
}
