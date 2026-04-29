package com.example.hr.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JSON Utility Class
 * Cung cấp các phương thức xử lý JSON
 */
@Slf4j
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Convert object to JSON string
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to JSON", e);
            return null;
        }
    }

    /**
     * Convert object to pretty JSON string
     */
    public static String toPrettyJson(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to pretty JSON", e);
            return null;
        }
    }

    /**
     * Convert JSON string to object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to object", e);
            return null;
        }
    }

    /**
     * Convert JSON string to object with TypeReference
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to object", e);
            return null;
        }
    }

    /**
     * Convert JSON string to List
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to list", e);
            return null;
        }
    }

    /**
     * Convert JSON string to Map
     */
    public static Map<String, Object> fromJsonToMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to map", e);
            return null;
        }
    }

    /**
     * Convert object to Map
     */
    public static Map<String, Object> toMap(Object object) {
        return objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * Convert Map to object
     */
    public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    /**
     * Deep copy object
     */
    public static <T> T deepCopy(T object, Class<T> clazz) {
        try {
            String json = objectMapper.writeValueAsString(object);
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error deep copying object", e);
            return null;
        }
    }

    /**
     * Merge two JSON objects
     */
    public static String mergeJson(String json1, String json2) {
        try {
            Map<String, Object> map1 = fromJsonToMap(json1);
            Map<String, Object> map2 = fromJsonToMap(json2);
            
            if (map1 == null || map2 == null) {
                return null;
            }
            
            map1.putAll(map2);
            return toJson(map1);
        } catch (Exception e) {
            log.error("Error merging JSON", e);
            return null;
        }
    }

    /**
     * Check if string is valid JSON
     */
    public static boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Get ObjectMapper instance
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Extract value from JSON by path
     */
    public static Object getValueByPath(String json, String path) {
        try {
            Map<String, Object> map = fromJsonToMap(json);
            if (map == null) {
                return null;
            }
            
            String[] keys = path.split("\\.");
            Object current = map;
            
            for (String key : keys) {
                if (current instanceof Map) {
                    current = ((Map<?, ?>) current).get(key);
                } else {
                    return null;
                }
            }
            
            return current;
        } catch (Exception e) {
            log.error("Error extracting value from JSON", e);
            return null;
        }
    }

    /**
     * Convert JSON array to List of Maps
     */
    public static List<Map<String, Object>> fromJsonArrayToListOfMaps(String json) {
        try {
            return objectMapper.readValue(json, 
                    new TypeReference<List<Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON array to list of maps", e);
            return null;
        }
    }

    /**
     * Minify JSON string (remove whitespace)
     */
    public static String minifyJson(String json) {
        try {
            Object obj = objectMapper.readValue(json, Object.class);
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error minifying JSON", e);
            return null;
        }
    }

    /**
     * Compare two JSON strings
     */
    public static boolean areJsonEqual(String json1, String json2) {
        try {
            Object obj1 = objectMapper.readValue(json1, Object.class);
            Object obj2 = objectMapper.readValue(json2, Object.class);
            return obj1.equals(obj2);
        } catch (JsonProcessingException e) {
            log.error("Error comparing JSON", e);
            return false;
        }
    }
}
