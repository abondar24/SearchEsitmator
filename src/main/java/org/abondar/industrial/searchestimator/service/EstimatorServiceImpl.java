package org.abondar.industrial.searchestimator.service;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.abondar.industrial.searchestimator.estimator.ScoreEstimator;
import org.abondar.industrial.searchestimator.model.AutoCompleteResponse;
import org.abondar.industrial.searchestimator.model.AutoCompleteSuggestion;
import org.abondar.industrial.searchestimator.model.Estimation;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of REST Service
 */
@Path("/")
public class EstimatorServiceImpl implements EstimatorService {


    @GET
    @Path("/estimator")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response estimate(@QueryParam("keyword") String keyword) {
        ScoreEstimator sc = new ScoreEstimator(keyword);

        keyword = keyword.replace("+", " ");

        sc.calcOccurrence(getAutoCompleteResults(keyword));
        sc.calcOccurrence(getAutoCompleteResults(keyword.substring(0, 1)));
        sc.calcOccurrence(getAutoCompleteResults(getHalfKeyword(keyword)));

        int score = sc.estimate();

        return Response.ok(new Estimation(keyword, score)).build();
    }

    private String getHalfKeyword(String keyword) {
        String halfKeyword = keyword.substring(0, keyword.length() / 2);

        if (halfKeyword.endsWith(" ")) {
            halfKeyword = halfKeyword +
                    keyword.substring(halfKeyword.length(), halfKeyword.length() + 1);
        }
        return halfKeyword;
    }

    private List<String> getAutoCompleteResults(String keyword) {
        WebClient client = WebClient
                .create(AmazonParamsUtil.ENDPOINT, Collections.singletonList(new JacksonJsonProvider()));

        client.path(AmazonParamsUtil.SUGGESTION_PATH)
                .query("session-id", AmazonParamsUtil.SESSION_ID)
                .query("customer-id", AmazonParamsUtil.CUSTOMER_ID)
                .query("request-id", AmazonParamsUtil.REQUEST_ID)
                .query("page-type", AmazonParamsUtil.PAGE_TYPE)
                .query("lop", AmazonParamsUtil.LANG)
                .query("site-variant", AmazonParamsUtil.SITE_VARIANT)
                .query("client-info", AmazonParamsUtil.CLIENT_INFO)
                .query("mid", AmazonParamsUtil.MID)
                .query("alias", AmazonParamsUtil.ALIAS)
                .query("suggestion-type", AmazonParamsUtil.SUGGESTION_TYPE)
                .query("b2b", AmazonParamsUtil.B2B)
                .query("fresh", AmazonParamsUtil.FRESH)
                .query("ks", AmazonParamsUtil.KS)
                .query("prefix", keyword)
                .query("fb", AmazonParamsUtil.FB)
                .query("_", AmazonParamsUtil.UNDERSCORE);

        Response response = client.get();

        AutoCompleteResponse resp = response.readEntity(AutoCompleteResponse.class);

        return resp.getSuggestions().stream().map(AutoCompleteSuggestion::getValue).collect(Collectors.toList());
    }
}


