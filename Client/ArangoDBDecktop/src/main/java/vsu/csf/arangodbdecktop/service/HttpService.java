package vsu.csf.arangodbdecktop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.bigtable.repackaged.com.google.gson.Gson;
import com.google.bigtable.repackaged.com.google.gson.GsonBuilder;
import javafx.scene.control.Alert;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import vsu.csf.arangodbdecktop.model.DataConnection;
import vsu.csf.arangodbdecktop.model.Quarry;
import vsu.csf.arangodbdecktop.model.adapter.DataConnectionAdapter;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message");
            alert.setHeaderText("Connected error!");
            alert.showAndWait();
            throw new RuntimeException(ex);
        }

        return response.getStatusLine().getStatusCode();
    }

    public Map<String, Map<String, Object>> doingQuarry(Quarry quarry) {
        String requestBody = "{" + "\"connection\" : {"+ quarry.getConnection()+ "}, " + quarry + "}";

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

        InputStream stream = null;
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

        //JSONObject object = new JSONObject(content.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Map<String, Object>>> typeReference =
                new TypeReference<>() {
                };
        Map<String, Map<String, Object>> map;
        try {
            map = objectMapper.readValue(content.toString(), typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    public Map<String, List<?>> getFileTree(DataConnection quarry) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DataConnection.class, new DataConnectionAdapter()).create();
        String json = gson.toJson(quarry);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(SERVER_URL + "/st");

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

        InputStream stream = null;
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

        JSONObject object = new JSONObject(content.toString());

        return jsonToMap(object);

    }

    public Map<String, Map<String, Object>> getCollectionTree(DataConnection quarry) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DataConnection.class, new DataConnectionAdapter()).create();
        String json = gson.toJson(quarry);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(SERVER_URL + "/cf");

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

        InputStream stream = null;
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

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Map<String, Object>>> typeReference =
                new TypeReference<>() {
                };
        Map<String, Map<String, Object>> map;
        try {
            map = objectMapper.readValue(content.toString(), typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    public static Map<String, List<?>> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<>();
        Map<String, List<?>> map = new HashMap<>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }

        for (Map.Entry<String, Object> item : retMap.entrySet()) {
            map.put(item.getKey(), convertObjectToList(item.getValue()));
        }

        return map;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[])obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>)obj);
        }
        return list;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public int createCollection(DataConnection data) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DataConnection.class, new DataConnectionAdapter()).create();
        String json = gson.toJson(data);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(SERVER_URL + "/create/Collection");

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

}
