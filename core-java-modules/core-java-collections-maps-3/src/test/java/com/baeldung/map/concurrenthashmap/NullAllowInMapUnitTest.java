package com.baeldung.map.concurrenthashmap;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.Test;

public class NullAllowInMapUnitTest {

    @Test
    public void allowNullKey_In_HashMapBackedSynchronizedMap() {
        Map<String, Integer> map = Collections
                .synchronizedMap(new HashMap<String, Integer>());
        map.put(null, 1);
        Assert.assertTrue(map.get(null).equals(1));
    }


    @Test(expected = NullPointerException.class)
    public void allowNullKey_In_TreeMapBackedSynchronizedMap() {
        Map<String, Integer> map = Collections.synchronizedMap(new TreeMap<String, Integer>());
        map.put(null, 1);
        Assert.assertTrue(map.get(null).equals(1));
    }
    
    @Test
    public void allowNullKey_In_LinkedHashMapBackedSynchronizedMap() {
        Map<String, Integer> map = Collections
                .synchronizedMap(new LinkedHashMap<String, Integer>());
        map.put(null, 1);
        Assert.assertTrue(map.get(null).equals(1));
    }
    
    @Test(expected = NullPointerException.class)
    public void allowNullKey_In_ConcurrentHasMap() {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        map.put(null, 1);
    }
    
    @Test
    public void allowNullValue_In_HashMapBackedSynchronizedMap() {
        Map<String, Integer> map = Collections.synchronizedMap(new HashMap<String, Integer>());
        map.put("1", null);
        Assert.assertNull(map.get("1"));
    }
    
    @Test
    public void allowNullValue_In_TreeMapBackedSynchronizedMap() {
        Map<String, Integer> map = Collections.synchronizedMap(new TreeMap<String, Integer>());
        map.put("1", null);
        Assert.assertNull(map.get("1"));
    }
    
    @Test
    public void allowNullValue_In_LinkedHashSynchronizedMap() {
        Map<String, Integer> map = Collections
                .synchronizedMap(new LinkedHashMap<String, Integer>());
        map.put("1", null);
        Assert.assertNull(map.get("1"));
    }
    
    @Test(expected = NullPointerException.class)
    public void allowNullValue_In_ConcurrentHasMap() {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        map.put("1", null);
    }

}