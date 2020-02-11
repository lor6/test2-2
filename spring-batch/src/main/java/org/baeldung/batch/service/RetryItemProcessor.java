package org.baeldung.batch.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.baeldung.batch.model.Transaction;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.io.IOException;

public class RetryItemProcessor implements ItemProcessor<Transaction, Transaction> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryItemProcessor.class);
    
    private static final int TWO_SECONDS = 2000;
    
    private CloseableHttpClient client;

    public RetryItemProcessor() {
        final RequestConfig config = RequestConfig.custom().setConnectTimeout(TWO_SECONDS).build();
        client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    @Override
    public Transaction process(Transaction transaction) throws IOException, JSONException {
        LOGGER.info("Attempting to process user with id={}", transaction.getUserId());
        HttpResponse response = fetchMoreUserDetails(transaction.getUserId());

        //parse user's age and postCode from response and update transaction
        String result = EntityUtils.toString(response.getEntity());
        JSONObject userObject = new JSONObject(result);
        transaction.setAge(Integer.parseInt(userObject.getString("age")));
        transaction.setPostCode(userObject.getString("postCode"));

        return transaction;
    }

    private HttpResponse fetchMoreUserDetails(int id) throws IOException {
        final HttpGet request = new HttpGet("http://www.baeldung.com:81/user/" + id);
        return client.execute(request);
    }
}
