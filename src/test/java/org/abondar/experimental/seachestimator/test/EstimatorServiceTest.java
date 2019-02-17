package org.abondar.experimental.seachestimator.test;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.abondar.industrial.searchestimator.Main;
import org.abondar.industrial.searchestimator.model.Estimation;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSBindingFactory;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collections;

import static org.junit.Assert.assertEquals;


@SpringBootTest(classes=Main.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EstimatorServiceTest {

    private Server server;

    private String endpoint = "local://estimator_test";


    @Before
    public void beforeMethod() {
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setBindingId(JAXRSBindingFactory.JAXRS_BINDING_ID);
        factory.setProvider(new JacksonJsonProvider());
        factory.setAddress(endpoint);
        factory.setServiceBean(new EstimatorServiceTestImpl());
        server = factory.create();
        server.start();
    }

    @Test
    public void testEcho() {
        String keyword="test";
        WebClient client = WebClient.create(endpoint, Collections.singletonList(new JacksonJsonProvider()));

        client.path("/estimator")
                .query("keyword",keyword)
                .accept(MediaType.APPLICATION_JSON);

        Response response = client.get();
        assertEquals(200, response.getStatus());

        Estimation es = response.readEntity(Estimation.class);
        assertEquals(keyword,es.getKeyword());
        assertEquals(50,es.getScore());
    }
}
