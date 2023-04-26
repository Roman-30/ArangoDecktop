package vsu.csf.arangodbdecktop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.bigtable.repackaged.com.google.gson.Gson;
import com.google.bigtable.repackaged.com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import vsu.csf.arangodbdecktop.controllers.DataConnection;
import vsu.csf.arangodbdecktop.controllers.Quarry;
import vsu.csf.arangodbdecktop.model.DataConnectionAdapter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HttpService {
    public static final String SERVER_URL = "http://localhost:8080";

    public int createDb(DataConnection data) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DataConnection.class, new DataConnectionAdapter()).create();
        String json = gson.toJson(data);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(SERVER_URL + "/create");

        post.setHeader("Content-type", "application/json");

        StringEntity entity;

        try {
            entity = new StringEntity(json);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }

        post.setEntity(entity);

        HttpResponse response;

        try {
            response = client.execute(post);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return response.getStatusLine().getStatusCode();
    }

    public int deleteDb(DataConnection data) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DataConnection.class, new DataConnectionAdapter()).create();
        String json = gson.toJson(data);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(SERVER_URL + "/delete");

        post.setHeader("Content-type", "application/json");

        StringEntity entity;

        try {
            entity = new StringEntity(json);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }

        post.setEntity(entity);

        HttpResponse response;

        try {
            response = client.execute(post);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return response.getStatusLine().getStatusCode();
    }

    public int checkConnectionDb(DataConnection data) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DataConnection.class, new DataConnectionAdapter()).create();
        String json = gson.toJson(data);

        System.out.println(json);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(SERVER_URL + "/check");

        post.setHeader("Content-type", "application/json");

        StringEntity entity;

        try {
            entity = new StringEntity(json);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }

        post.setEntity(entity);

        HttpResponse response;

        try {
            response = client.execute(post);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return response.getStatusLine().getStatusCode();
    }

    public int doingQuarry(DataConnection connection, Quarry quarry) {
//        Gson gson = new GsonBuilder().registerTypeAdapter(DataConnection.class, new DataConnectionAdapter()).create();
//        String json = gson.toJson(data);

//        String requestBody = "{" + "\"connection\" : {"+ connection.toString() + "}, " + quarry.toString() + "}";

        List<Object> objects = new ArrayList<>( )  {{
            add(connection);
            add(quarry);
        }};

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(objects);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(requestBody);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(SERVER_URL + "/quarry");

        post.setHeader("Content-type", "application/json");

        StringEntity entity;

        try {
            entity = new StringEntity(requestBody);

        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }

        post.setEntity(entity);

        HttpResponse response;

        try {
            response = client.execute(post);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return response.getStatusLine().getStatusCode();
    }

}
