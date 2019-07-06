package com.garrettestrin.sample.biz;

import com.garrettestrin.sample.api.ApiObjects.Sample;
import com.garrettestrin.sample.data.SampleDao;

public class SampleService {

    private final SampleDao sampleDao;

    public SampleService(SampleDao sampleDao) {
        this.sampleDao = sampleDao;
    }

    public Sample sample(String string){
        return new Sample(sampleDao.getUserId(string));
    }
}
