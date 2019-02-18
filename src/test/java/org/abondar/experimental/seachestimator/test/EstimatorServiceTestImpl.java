package org.abondar.experimental.seachestimator.test;

import org.abondar.industrial.searchestimator.model.Estimation;
import org.abondar.industrial.searchestimator.service.EstimatorService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST service test implementation
 */
@Path("/")
public class EstimatorServiceTestImpl implements EstimatorService {

    /**
     *
     * @param keyword - search keyword
     * @return test data
     */
    @GET
    @Path("/estimator")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response estimate(@QueryParam("keyword") String keyword) {
        return Response.ok(new Estimation(keyword, 50)).build();
    }
}
