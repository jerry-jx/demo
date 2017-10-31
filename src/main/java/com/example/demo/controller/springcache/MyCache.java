package com.example.demo.controller.springcache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

public class MyCache implements Cache {

    private String name;
    private Map<String, Object> store = new ConcurrentHashMap<String, Object>();;

    public MyCache() {
    }

    public MyCache(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public void evict(Object obj) {
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper result = null;
        Object thevalue = store.get(key);
        if (thevalue != null) {
            result = new SimpleValueWrapper(thevalue);
        }
        return result;
    }

    @Override
    public <T> T get(Object key, Class<T> clazz) {
        return clazz.cast(store.get(key));
    }

    @Override
    public <T> T get(Object o, Callable<T> callable) {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return store;
    }

    @Override
    public void put(Object key, Object value) {
        store.put((String) key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        put(key, value);
        return new SimpleValueWrapper(value);
    }
}
