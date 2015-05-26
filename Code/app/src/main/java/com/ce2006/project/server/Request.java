package com.ce2006.project.server;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * use {@link org.apache.http.client.methods.HttpPost} to request
 * and returning content from {@link org.apache.http.HttpEntity} in {@link org.apache.http.HttpResponse}
 * Created by lhtan on 24/3/15.
 */
public class Request {
    /**
     * address of the hosted server
     */
    private static final String host = "http://172.22.135.137:8000/";
    private String url;
    private HashMap<String, String> params;

    protected Request(String url) {
        this.url = host + url;
        params = new HashMap<>();
    }

    public static Request createRequest(String url) {
        Request request = new Request(url);
        return request;
    }

    public Request data(String key, String value) {
        params.put(key, value);
        return this;
    }

    /**
     * sending a {@link org.apache.http.client.methods.HttpPost} to {@link com.ce2006.project.server.Request#url}
     * and returning the content from {@link org.apache.http.HttpEntity} in {@link org.apache.http.HttpResponse}
     * @return string
     */
    public Object execute() {
        try {
            HttpPost request = new HttpPost(url);
            // Request parameters and other properties.
            List<NameValuePair> params = new ArrayList<NameValuePair>(this.params.size());
            for (String key : this.params.keySet()) {
                params.add(new BasicNameValuePair(key, this.params.get(key)));
            }
            request.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

            //client
            HttpClient client = new DefaultHttpClient();

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                return "";
            }
            if (entity != null) {
                String content = EntityUtils.toString(entity);
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}