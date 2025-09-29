package com.iexceed.appzillon.accounts.utils;

import java.util.Map;

public class MapUtils {
    public static String getString(Map<String, Object> map, String key) {
        if (map == null || key == null) return null;
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }
    public static Object getValue(Map<String, Object> map, String key) {
        if (map == null || key == null) return null;
        return map.get(key);
    }
}
