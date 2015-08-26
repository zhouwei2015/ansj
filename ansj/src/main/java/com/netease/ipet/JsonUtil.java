package com.netease.ipet;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JsonUtil
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String getJsonString(Object object){
        String jsonString = null;
        try {
            jsonString = OBJECT_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonString == null ? "{}" : jsonString;
    }

    /**
     * 按照给定格式类型格式读出对象.
     * @param <T>
     * @param content
     * @param clazz
     * @return
     */
    public static <T> T readObjectFromString(String content, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(content, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}