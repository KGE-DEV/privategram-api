package com.garrettestrin.PrivateGram.data;

import com.garrettestrin.PrivateGram.data.DataObjects.Invite;
import com.garrettestrin.PrivateGram.data.DataObjects.ResetPasswordToken;
import com.garrettestrin.PrivateGram.data.DataObjects.User;
import java.util.Date;
import java.util.List;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UserDao extends SqlObject {

    // TODO: JAVADOC
    @SqlQuery("SELECT password "
            + "FROM users "
            + "WHERE email = :email")
    String getHashedPasswordByEmail(@Bind("email") String email);

    // TODO: JAVADOC
    @SqlUpdate("INSERT INTO "
            + "reset_password_token (email, token, expiration) "
            + "VALUES (:email, :token, :expiration)")
    boolean saveResetEmailToken(@Bind("email") String email, @Bind("token") String token, @Bind("expiration") Date expiration);

    // TODO: JAVADOC
    @SqlQuery("SELECT token "
            + "FROM reset_password_token "
            + "WHERE email = :email")
    @RegisterBeanMapper(ResetPasswordToken.class)
    ResetPasswordToken checkForExistingResetToken(@Bind("email") String email);

    // TODO: JAVADOC
    @SqlUpdate("DELETE FROM reset_password_token "
            + "WHERE email = :email")
    boolean deleteExistingResetPasswordToken(@Bind("email") String email);

    // TODO: JAVADOC
    @SqlUpdate("INSERT INTO "
            + "users (name, email, password, wp_pass) "
            + "VALUES (:name, :email, :password, false)")
    boolean registerUser(@Bind("email") String email, @Bind("name") String name, @Bind("password") String password);

    @SqlQuery("SELECT role FROM users WHERE id = :user_id")
    String getUserRole(@Bind("user_id") int id);

    @SqlQuery("SELECT * "
    +   "FROM users "
    +   "WHERE email = :email")
    @RegisterBeanMapper(User.class)
  User getUserByEmail(@Bind("email") String email);

  @SqlUpdate("INSERT INTO "
          + "invites (name, email) "
          + "VALUES (:name, :email)")
  boolean saveInvite(@Bind("email") String email, @Bind("name") String name);

  @SqlUpdate("Update `users` "
          + "SET password = :password, wp_pass = 0 "
          + "WHERE email = :email")
  boolean resetPassword(@Bind("email") String email, @Bind("password") String password);

  @SqlQuery("SELECT * "
          +   "FROM users "
          +   "WHERE `role` = 'admin'")
  @RegisterBeanMapper(User.class)
  List<User> getAdminUsers();

  @SqlQuery("SELECT id, email, name "
          +   "FROM invites "
          +   "WHERE `active` = 1")
  @RegisterBeanMapper(Invite.class)
  List<Invite> getInvites();

  @SqlQuery("Select * "
          + "FROM invites "
          + "WHERE email = :email")
  @RegisterBeanMapper(Invite.class)
  Invite checkForExistingInvite(@Bind("email") String email);

  @SqlUpdate("Update `invites` "
            + "SET active = 0 "
            + "WHERE email = :email")
  boolean deactivateUserInvite(@Bind("email") String email);

  @SqlQuery("SELECT id, name, email, role, wp_pass "
          + "FROM users "
          + "WHERE active = 1")
  @RegisterBeanMapper(com.garrettestrin.PrivateGram.api.ApiObjects.User.class)
  List<com.garrettestrin.PrivateGram.api.ApiObjects.User> getAllUsers();

  @SqlUpdate("Update `users` "
          + "SET name = :name, email = :email, role = :role "
          + "WHERE id = :id")
  boolean updateUser(@Bind("id") Long id, @Bind("name") String name, @Bind("email") String email, @Bind("role") String role);

  @SqlUpdate("Update `users` "
            + "SET active = 0 "
            + "WHERE id = :id")
  boolean deactivateUser(@Bind("id") int id);
}
