package com.tt.sharedbaseclass.model;

import android.util.LruCache;

import java.io.Serializable;

/**
 * Created by zhengguo on 2016/6/7.
 */
public class RenderLruCache<String, RenderObjectBeans> extends LruCache implements Serializable {
    public RenderLruCache(int maxSize) {
        super(maxSize);
    }

}
