package com.garrettestrin.sample.api;

import com.codahale.metrics.annotation.Timed;
import com.garrettestrin.sample.api.ApiObjects.Sample;
import com.garrettestrin.sample.biz.SampleService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/sampleRootPath")
@Produces(MediaType.APPLICATION_JSON)
public class SampleResource {

    private SampleService sampleService;

    public SampleResource(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @GET
    @Path("specificPath/{name}")
    @Timed
    public Sample sample(@PathParam("name") String string) {
        return sampleService.sample(string);
    }
}