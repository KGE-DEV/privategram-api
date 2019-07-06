package com.garrettestrin.sample.biz;


import com.garrettestrin.sample.api.ApiObjects.Sample;
import com.garrettestrin.sample.data.SampleDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SampleServiceTest {
    private final String TEST_ID = "3";

    @Mock
    private SampleDao sampleDao;

    @Before
    public void setUp() throws Exception {
        when(sampleDao.getUserId(Mockito.anyString())).thenReturn(TEST_ID);
    }

    @Test
    public void testSample() throws Exception {
        Sample sample = new Sample(sampleDao.getUserId("Garrett"));
        assertEquals(sample.getId(), "3");
    }
}
