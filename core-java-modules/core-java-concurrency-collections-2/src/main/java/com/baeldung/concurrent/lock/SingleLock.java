package com.baeldung.concurrent.lock;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.base.Supplier;

public class SingleLock extends ConcurrentAccessExperiment {
    ReentrantLock lock;

    public SingleLock() {
        lock = new ReentrantLock();
    }

    protected Supplier<?> putSupplier(Map<String,String> map, int key) {
        return (()-> {
            try {
                lock.lock();
                map.put("key" + key, "value" + key);
            } finally {
                lock.unlock();
            }
            return null;
        });
    }

    protected Supplier<?> getSupplier(Map<String,String> map, int key) {
        return (()-> {
            try {
                lock.lock();
                map.get("key" + key);
            } finally {
                lock.unlock();
            }
            return null;
        });
    }
}
