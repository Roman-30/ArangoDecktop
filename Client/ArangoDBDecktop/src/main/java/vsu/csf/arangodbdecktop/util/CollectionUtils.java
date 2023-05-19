package vsu.csf.arangodbdecktop.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class CollectionUtils {
    public static Map<String, List<?>> jsonToMap(JSONObject json)  {
        Map<String, Object> retMap = new HashMap<>();
        Map<String, List<?>> map = new HashMap<>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }

        for (Map.Entry<String, Object> item : retMap.entrySet()) {
            map.put(item.getKey(), convertObjectToList(item.getValue()));
        }

        return map;
    }

    public static Map<String, Object> toMap(JSONObject object)  {
        Map<String, Object> map = new HashMap<>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>) obj);
        }
        return list;
    }

    public static List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
