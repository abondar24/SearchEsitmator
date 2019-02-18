package org.abondar.industrial.searchestimator.service;


import javax.ws.rs.core.Response;


public interface EstimatorService {

    public Response estimate(String keyword);
}
