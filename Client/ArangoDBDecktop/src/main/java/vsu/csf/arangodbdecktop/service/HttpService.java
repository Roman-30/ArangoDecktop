package vsu.csf.arangodbdecktop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.bigtable.repackaged.com.google.gson.Gson;
import com.google.bigtable.repackaged.com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import vsu.csf.arangodbdecktop.util.CollectionUtils;
import vsu.csf.arangodbdecktop.model.DataConnection;
import vsu.csf.arangodbdecktop.model.Quarry;
import vsu.csf.arangodbdecktop.model.DataConnectionAdapter;

import java.io.*;
import java.util.*;

public class HttpService {
    private static final String SERVER_URL = "http://localhost:8080";

    private HttpResponse createBaseHttpResponse(String json, String url) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(SERVER_URL + url);

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

        return response;
    }

    private int createSimpleHttpRequest(String json, String url) {
        HttpResponse response = createBaseHttpResponse(json, url);

        return response.getStatusLine().getStatusCode();
    }

    private String createCustomHttpRequest(String requestBody, String name) {
        HttpResponse response = createBaseHttpResponse(requestBody, name);

        InputStream stream;
        try {
            stream = response.getEntity().getContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringBuilder content = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(stream));
            String line;

            while ((line = bf.readLine()) != null) {
                content.append(line).append("\n");
            }
            bf.close();
        } catch (Exception e) {
            System.out.println("Ошибка");
        }
        return content.toString();
    }

    public int createDb(DataConnection data) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DataConnection.class, new DataConnectionAdapter()).create();
        String json = gson.toJson(data);

        return createSimpleHttpRequest(json, "/database/create");
    }

    public int createCollection(DataConnection data) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DataConnection.class, new DataConnectionAdapter()).create();
        String json = gson.toJson(data);

        return createSimpleHttpRequest(json, "/collection/create");
    }

    public int deleteCollection(DataConnection data) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DataConnection.class, new DataConnectionAdapter()).create();
        String json = gson.toJson(data);

        return createSimpleHttpRequest(json, "/collection/delete");
    }

    public int deleteDb(DataConnection data) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DataConnection.class, new DataConnectionAdapter()).create();
        String json = gson.toJson(data);

        return createSimpleHttpRequest(json, "/database/delete");
    }

    public int checkConnectionDb(DataConnection data) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DataConnection.class, new DataConnectionAdapter()).create();
        String json = gson.toJson(data);

        return createSimpleHttpRequest(json, "/database/check");
    }

    public List<?> doingQuarry(Quarry quarry) {
        String requestBody = "{" + "\"connection\" : {" + quarry.getConnection() + "}, " + quarry + "}";

        String content = createCustomHttpRequest(requestBody, "/quarry");

        String line = content.substring(1, content.length() - 2);

        if (line.charAt(0) == '{') {

            String[] jsons = line.split("},");
            TypeReference<Map<Object, Object>> typeReference = new TypeReference<>() {
            };
            ObjectMapper objectMapper = new ObjectMapper();
            Map<Object, Object> map;

            List<Map<Object, Object>> exp = new ArrayList<>();

            for (String s : jsons) {
                try {
                    map = objectMapper.readValue(s + "}", typeReference);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                exp.add(map);
            }

            return exp;
        } else if (line.charAt(0) == '[') {
            return new ArrayList<>(List.of(line.split(",")));
        } else {
            return new ArrayList<>();
        }
    }

    public Map<String, List<?>> getFileTree(DataConnection quarry) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DataConnection.class, new DataConnectionAdapter()).create();
        String json = gson.toJson(quarry);

        String content = createCustomHttpRequest(json, "/database/files");

        JSONObject object = new JSONObject(content);

        return CollectionUtils.jsonToMap(object);

    }

    public Map<String, Map<String, Object>> getCollectionTree(DataConnection quarry) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DataConnection.class, new DataConnectionAdapter()).create();
        String json = gson.toJson(quarry);

        String content = createCustomHttpRequest(json, "/collection/files");

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Map<String, Object>>> typeReference =
                new TypeReference<>() {
                };
        Map<String, Map<String, Object>> map;
        try {
            map = objectMapper.readValue(content, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
