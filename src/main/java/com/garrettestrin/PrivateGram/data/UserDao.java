package com.garrettestrin.PrivateGram.data;

import com.garrettestrin.PrivateGram.api.ApiObjects.User;
import com.garrettestrin.PrivateGram.data.DataObjects.ResetPasswordToken;
import org.hibernate.annotations.SQLUpdate;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Date;

public interface UserDao extends SqlObject {

//    // TODO: JAVADOC
//    @SqlQuery("SELECT * "
//        +   "FROM users "
//        +   "WHERE id = :id")
//    @RegisterBeanMapper(User.class)
//    User getUserById(@Bind("id") long id);
//
//    // TODO: JAVADOC
//    @SqlQuery("SELECT id "
//            +   "FROM users "
//            +   "WHERE email = :email")
//    @RegisterBeanMapper(User.class)
//    Long getUserIdByEmail(@Bind("email") String email);
//
//    // TODO: JAVADOC
//    @SqlQuery("SELECT password "
//            + "FROM users "
//            + "WHERE email = :email")
//    String getHashedPasswordByEmail(@Bind("email") String email);
//
//    // TODO: JAVADOC
//    @SqlUpdate("INSERT INTO "
//            + "reset_email (email, token, expiration) "
//            + "VALUES (:email, :token, :expiration)")
//    boolean resetPassword(@Bind("email") String email, @Bind("token") String token, @Bind("expiration") Date expiration);
//
//    // TODO: JAVADOC
//    @SqlQuery("SELECT token, expiration "
//            + "FROM reset_email "
//            + "WHERE email = :email")
//    @RegisterBeanMapper(ResetPasswordToken.class)
//    ResetPasswordToken checkForExistingResetToken(@Bind("email") String email);
//
//    // TODO: JAVADOC
//    @SqlUpdate("DELETE FROM reset_email "
//            + "WHERE email = :email")
//    boolean deleteExistingResetPasswordToken(@Bind("email") String email);

    // TODO: JAVADOC
    @SqlUpdate("INSERT INTO "
            + "users (id, name, email, password) "
            + "VALUES (:id, :name, :email, :password)")
    boolean registerUser(@Bind("id") int id, @Bind("email") String email, @Bind("name") String name, @Bind("password") String password);

    @SqlQuery("SELECT role FROM users WHERE id = :user_id")
    String getUserRole(@Bind("user_id") String id);
}
