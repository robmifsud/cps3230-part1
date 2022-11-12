package com.ecommerce.alerts;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class Alerts {
    String url;

    public Alerts(String url){
        this.url = url;
    }

    public int uploadAlerts(LinkedList<HashMap<String,Object>> alerts) throws IOException {
        int verify = 0;

        if(alerts.size()!=0){
            for(HashMap<String, Object> alert: alerts){
                JSONObject json = new JSONObject(alert);
                String jsonTemp = json.toString();
                StringEntity entity = new StringEntity(jsonTemp, ContentType.APPLICATION_JSON);

                HttpPost request = new HttpPost(url);
                request.setEntity(entity);
                System.out.println(request);


                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpResponse response = httpClient.execute(request);
                System.out.println(response.toString());

                if(response.getCode() == 201){
                    verify++;
                }
            }
        }

        return verify;
    }

    public int deleteAlerts() throws IOException {
        HttpDelete request = new HttpDelete(url);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(request);
        System.out.println(response.toString());

        return response.getCode();
    }
}
