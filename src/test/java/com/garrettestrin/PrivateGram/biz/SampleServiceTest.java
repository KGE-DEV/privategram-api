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
public class SampleServiceTest {
    private final long TEST_ID = 1L;

    @Mock
    private UserDao userDao;

    @Before
    public void setUp() throws Exception {
        when(userDao.getUser(TEST_ID)).thenReturn(new User(1L, "Garrett", "garrett.estrin@gmail.com"));
    }

    @Test
    public void testGetUser() throws Exception {
        User user = userDao.getUser(TEST_ID);
        assertEquals(user.getId(), 1L);
        assertEquals(user.getName(), "Garrett");
        assertEquals(user.getEmail(), "garrett.estrin@gmail.com");
    }
}
