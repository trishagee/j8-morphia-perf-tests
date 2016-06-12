package com.mechanitis.undertest;


import org.bson.BSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Helper to allow for optimizations for different types of Map/Collections
 *
 * @author Scott Hernandez
 */
public final class IterHelper<K, V> {
    /**
     * Process a Map
     *
     * @param x        the object to process
     * @param callback the callback
     */
    @SuppressWarnings("unchecked")
    public static <K, V> void loopMapSimplified(final Map x, final MapIterCallback<K, V> callback) {
        final Map<K, V> m = (Map<K, V>) x;
        for (final Entry<K, V> entry : m.entrySet()) {
            callback.eval(entry.getKey(), entry.getValue());
        }
    }

    public void loopMap(final Object x, final MapIterCallback<K, V> callback) {
        if (x == null) {
            return;
        }

        if (x instanceof Collection) {
            throw new IllegalArgumentException("call loop instead");
        }

        if (x instanceof HashMap<?, ?>) {
            if (((HashMap) x).isEmpty()) {
                return;
            }

            final HashMap<?, ?> hm = (HashMap<?, ?>) x;
            for (final Entry<?, ?> e : hm.entrySet()) {
                callback.eval((K) e.getKey(), (V) e.getValue());
            }
            return;
        }
        if (x instanceof Map) {
            final Map<K, V> m = (Map<K, V>) x;
            for (final Entry<K, V> entry : m.entrySet()) {
                callback.eval(entry.getKey(), entry.getValue());
            }
            return;
        }
        if (x instanceof BSONObject) {
            final BSONObject m = (BSONObject) x;
            for (final String k : m.keySet()) {
                callback.eval((K) k, (V) m.get(k));
            }
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
