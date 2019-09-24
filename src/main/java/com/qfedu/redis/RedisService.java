package com.qfedu.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    public void setBalance(String key, String code, Double balance){
        Jedis jedis = jedisPool.getResource();
        jedis.hset(key,code,balance.toString());
        jedis.close();
    }

    public Double getBalance(String key, String code){
        Jedis jedis = jedisPool.getResource();
        Double balance = null;
        try {
            balance = Double.parseDouble(jedis.hget(key,code));
        } catch (Exception e) {

        }
        return balance;
    }
}
