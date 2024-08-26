package com.tbread.facechat.util;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
* cache 서버 대용 임시 클래스
**/
public class ExpiringHashMap<K, V> {
    private final ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void put(K key, V value, Date removalDate) {
        map.put(key, value);
        long delay = removalDate.getTime() - System.currentTimeMillis();
        if (delay > 0) {
            scheduler.schedule(() -> {
                map.remove(key);
            }, delay, TimeUnit.MILLISECONDS);
        } else {
            map.remove(key);
            System.out.println("Key \""+key+"\" removed immediately.");
        }
    }

    public V get(K key){
        return map.get(key);
    }

}
