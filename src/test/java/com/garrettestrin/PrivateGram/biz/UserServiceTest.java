package com.garrettestrin.PrivateGram.biz;


import com.garrettestrin.PrivateGram.api.ApiObjects.User;
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

    @Before
    public void setUp() throws Exception {
        when(userDao.getUserById(TEST_ID)).thenReturn(new User(1L, "gtest@gmail.com", "garrett", "estrin", "badpassword"));
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
}
