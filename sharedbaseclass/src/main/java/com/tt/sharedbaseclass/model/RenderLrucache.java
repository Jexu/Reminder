package com.tt.sharedbaseclass.model;

import android.util.LruCache;

import java.io.Serializable;

/**
 * Created by zhengguo on 2016/6/7.
 */
public class RenderLruCache<K, V> extends LruCache implements Serializable {
    public RenderLruCache(int maxSize) {
        super(maxSize);
    }

    public void addToCache(K key, V value) {
        put(key, value);
    }

    public V getFromCache(K key) {
        return (V) get(key);
    }
}
