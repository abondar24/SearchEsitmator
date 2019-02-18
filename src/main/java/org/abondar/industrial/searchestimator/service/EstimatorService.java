package org.abondar.industrial.searchestimator.service;


import javax.ws.rs.core.Response;

/**
 * REST service for estimation of score
 */
public interface EstimatorService {

    /**
     * API method for score estimation
     * @param keyword - search keyword
     * @return Response with keyword and score in JSON format
     */
    public Response estimate(String keyword);
}
