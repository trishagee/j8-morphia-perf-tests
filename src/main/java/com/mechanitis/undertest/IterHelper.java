package com.mechanitis.undertest;


import java.util.Map;
import java.util.Map.Entry;


/**
 * Helper to allow for optimizations for different types of Map/Collections
 *
 * @author Scott Hernandez
 */
public final class IterHelper {
    /**
     * Process a Map
     *
     * @param x        the object to process
     * @param callback the callback
     */
    @SuppressWarnings("unchecked")
    public static <K, V> void loopMap(final Map x, final MapIterCallback<K, V> callback) {
        final Map<K, V> m = (Map<K, V>) x;
        for (final Entry<K, V> entry : m.entrySet()) {
            callback.eval(entry.getKey(), entry.getValue());
        }
    }

    /**
     * A callback mechanism for processing Maps
     *
     * @param <K> the type map keys
     * @param <V> the type map values
     */
    @FunctionalInterface
    public interface MapIterCallback<K, V> {
        /**
         * The method to call in the callback
         *
         * @param k the key from the map
         * @param v the value for the key
         */
        void eval(K k, V v);
    }

}
