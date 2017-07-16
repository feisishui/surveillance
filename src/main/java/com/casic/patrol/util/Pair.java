package com.casic.patrol.util;

/**
 * Created by lenovo on 2017/1/18.
 */
public class Pair <K, V> {
    private K key;
    private V value;

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Pair(K k, V v) {
        key = k;
        value = v;
    }

    public String toString() {
        return "{" + key + ":" + value + "}";
    }
}
