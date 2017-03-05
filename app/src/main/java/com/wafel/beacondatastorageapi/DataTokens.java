package com.wafel.beacondatastorageapi;

import java.io.Serializable;


@SuppressWarnings({"WeakerAccess", "unused"})
public class DataTokens {

    public interface DataToken<DATA extends Serializable> {
        String getName();
        DATA getType();
    }

    public static DataToken<String> forString(String name) {
        return new SimpleDataToken<>(String.class, name);
    }

    public static DataToken<Long> forLong(String name) {
        return new SimpleDataToken<>(Long.class, name);
    }

    public static DataToken<Integer> forInt(String name) {
        return new SimpleDataToken<>(Integer.class, name);
    }

    public static <DATA extends Serializable> DataToken<DATA> forType(Class<DATA> type, String name) {
        return new SimpleDataToken<>(type, name);
    }

}
