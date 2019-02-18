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
                .create(AmazonCredsUtil.ENDPOINT, Collections.singletonList(new JacksonJsonProvider()));

        client.path(AmazonCredsUtil.SUGGESTION_PATH)
                .query("session-id", AmazonCredsUtil.SESSION_ID)
                .query("customer-id", AmazonCredsUtil.CUSTOMER_ID)
                .query("request-id", AmazonCredsUtil.REQUEST_ID)
                .query("page-type", AmazonCredsUtil.PAGE_TYPE)
                .query("lop", AmazonCredsUtil.LANG)
                .query("site-variant", AmazonCredsUtil.SITE_VARIANT)
                .query("client-info", AmazonCredsUtil.CLIENT_INFO)
                .query("mid", AmazonCredsUtil.MID)
                .query("alias", AmazonCredsUtil.ALIAS)
                .query("suggestion-type", AmazonCredsUtil.SUGGESTION_TYPE)
                .query("b2b", AmazonCredsUtil.B2B)
                .query("fresh", AmazonCredsUtil.FRESH)
                .query("ks", AmazonCredsUtil.KS)
                .query("prefix", keyword)
                .query("fb", AmazonCredsUtil.FB)
                .query("_", AmazonCredsUtil.UNDERSCORE);

        Response response = client.get();

        AutoCompleteResponse resp = response.readEntity(AutoCompleteResponse.class);

        return resp.getSuggestions().stream().map(AutoCompleteSuggestion::getValue).collect(Collectors.toList());
    }
}


