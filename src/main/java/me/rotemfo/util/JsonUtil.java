package me.rotemfo.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public final class JsonUtil {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public static <T> T fromJson(final String json, final Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json, classOfT);
    }
    public static <T> T fromJson(final byte[] bytes, final Class<T> classOfT) throws JsonSyntaxException {
        return fromJson(new String(bytes), classOfT);
    }

    public static String toJson(final Object o) {
        return gson.toJson(o);
    }
}
