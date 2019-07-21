package com.garrettestrin.PrivateGram.biz;


import com.garrettestrin.PrivateGram.api.ApiObjects.User;
import com.garrettestrin.PrivateGram.app.Auth.Auth;
import com.garrettestrin.PrivateGram.app.PrivateGramConfiguration;
import com.garrettestrin.PrivateGram.data.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    private final long TEST_ID = 1L;

    @Mock
    private UserDao userDao;

    private Auth auth;
    private PrivateGramConfiguration config;
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        when(userDao.getUserById(TEST_ID)).thenReturn(new User(1L, "gtest@gmail.com", "garrett", "estrin", "badpassword"));
        this.config = new PrivateGramConfiguration();
        this.auth = new Auth(this.config);
        this.userService = new UserService(this.userDao, this.auth, this.config);
    }

    @Test
    public void testGetUser() throws Exception {
        User user = userDao.getUserById(TEST_ID);
        assertEquals(user.getId(), 1L);
        assertEquals(user.getFirst_name(), "garrett");
        assertEquals(user.getLast_name(), "estrin");
        assertEquals(user.getEmail(), "gtest@gmail.com");
        assertEquals(user.getPassword(), "badpassword");
    }

    @Test
    public void testValidatePassword() throws Exception {
        boolean pw1 = userService.validatePassword("password");
        assertEquals(pw1, false);

        boolean pw2 = userService.validatePassword("password1");
        assertEquals(pw2, false);

        boolean pw3 = userService.validatePassword("1");
        assertEquals(pw3, false);

        boolean pw4 = userService.validatePassword("Password1");
        assertEquals(pw4, true);
    }

    @Test
    public void testValidateEmail() throws Exception {
        boolean em1 = userService.validateEmail("g");
        assertEquals(em1, false);

        boolean em2 = userService.validateEmail("g@mail.");
        assertEquals(em2, false);

        boolean em3 = userService.validateEmail("gtest@mail.com");
        assertEquals(em3, true);
    }
}
