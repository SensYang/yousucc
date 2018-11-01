package com.litesuits.common.net;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by SensYang on 2016/3/30 0030.
 */
public class ParseJson2BeanUtils {
    static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseJson2Bean(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
