package com.nowcoder.wenda.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class JedisAdapter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool;

    public static void print(int index, Object obj) {
        System.out.println(String.format("%d, %s", index, obj.toString()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost", 6379);

    }

    public Jedis getJedis() {
        return pool.getResource();
    }

    public Transaction multi(Jedis jedis) {
        try {
            jedis.auth("root");
            jedis.select(9);
            return jedis.multi();
        } catch (Exception e) {
            logger.error("发生异常 in multi", e.getMessage());
        }
        return null;
    }

    public List<Object> exec(Transaction tx, Jedis jedis) {
        try {
            return tx.exec();
        } catch (Exception e) {
            logger.error("发生异常 in exec", e.getMessage());
        } finally {
            if (tx != null) {
                try {
                    tx.close();
                } catch (IOException e) {
                    logger.error("发生异常 in close tx", e.getMessage());
                }
            }
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long zadd(String key, double score, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.auth("root");
            jedis.select(9);
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
            logger.error("发生异常:sadd:", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Set<String> zrevrange(String key, int start, int count) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.auth("root");
            jedis.select(9);
            return jedis.zrevrange(key, start, count);
        } catch (Exception e) {
            logger.error("发生异常:sadd:", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long zcard(String key, int start, int count) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.auth("root");
            jedis.select(9);
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常:sadd:", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.auth("root");
            jedis.select(9);
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常:sadd:", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.auth("root");
            jedis.select(9);
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error("发生异常:sadd:", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.auth("root");
            jedis.select(9);
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常:sadd:", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.auth("root");
            jedis.select(9);
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常:srem:", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }


    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.auth("root");
            jedis.select(9);
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常:scard:", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismemeber(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.auth("root");
            jedis.select(9);
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常:sismember:", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.auth("root");
            jedis.select(9);
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常:brpop:" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.auth("root");
            jedis.select(9);
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常:lpush:" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }


//    public static  void main(String[] args){
//        Jedis jedis = new Jedis("redis://localhost:6379/9");
//        Jedis jedis = new Jedis("localhost", 6379);
//        jedis.auth("root");
//        jedis.select(9);
//        jedis.flushDB();
//
//        jedis.set("hello", "world");
//        print(1,jedis.get("hello"));
//        jedis.rename("hello","newhello");
//        print(1, jedis.get("newhello"));
//        jedis.setex("hello2", 15, "world");
//
//        jedis.set("pv","100");
//        jedis.incr("pv");
//        jedis.incrBy("pv", 5);
//        jedis.decrBy("pv", 3);
//        print(2, jedis.get("pv"));
//
//        print(3, jedis.keys("*"));
//
//        String listName = "list";
//        jedis.del(listName);
//        for(int i = 0; i < 10; ++i){
//            jedis.lpush(listName, "a" + String.valueOf(i));
//        }
//        print(4, jedis.lrange(listName, 0, 12));
//        print(5,jedis.llen(listName));
//        print(6, jedis.lpop(listName));
//        print(7, jedis.llen(listName));
//        print(8, jedis.lrange(listName, 2, 6));
//        print(9, jedis.lindex(listName, 1));
//        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4","cc"));
//        print(11, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "aa"));
//        print(12, jedis.lrange(listName, 0, 12));
//
//        String userKey = "userxx";
//        jedis.hset(userKey, "name", "xiaoming");
//        jedis.hset(userKey, "age", "18");
//        jedis.hset(userKey, "phone", "13888888888");
//        print(13, jedis.hget(userKey,"name"));
//        print(14, jedis.hgetAll(userKey));
//        jedis.hdel(userKey, "phone");
//        print(15, jedis.hgetAll(userKey));
//        print(16, jedis.hexists(userKey, "name"));
//        print(17, jedis.hexists(userKey, "email"));
//        print(18, jedis.hkeys(userKey));
//        print(19, jedis.hvals(userKey));
//        print(20, jedis.hsetnx(userKey, "name","xiaohong"));
//        print(21, jedis.hsetnx(userKey, "school", "qinghua"));
//        print(22, jedis.hgetAll(userKey));
//
//        String likeKey1 = "commentLike1";
//        String likeKey2 = "commentLike2";
//
//        for(int i = 0; i < 10; ++i){
//            jedis.sadd(likeKey1, String.valueOf(i));
//            jedis.sadd(likeKey2, String.valueOf(i * i));
//        }
//        print(23,jedis.smembers(likeKey1));
//        print(24,jedis.smembers(likeKey2));
//
//        print(25, jedis.sunion(likeKey1, likeKey2));
//        print(26, jedis.sdiff(likeKey1, likeKey2));
//        print(27,jedis.sinter(likeKey1,likeKey2));
//
//        print(28,jedis.sismember(likeKey1,"1"));
//        print(29,jedis.sismember(likeKey1,"88"));
//        jedis.srem(likeKey1,"5");
//        print(30, jedis.smembers(likeKey1));
//        jedis.smove(likeKey2, likeKey1, "25");
//        print(31, jedis.smembers(likeKey1));
//        print(32, jedis.smembers(likeKey2));
//        print(33,jedis.scard(likeKey1));
//
//        String randKey = "randKey";
//        jedis.zadd(randKey, 15, "jim");
//        jedis.zadd(randKey, 3, "bob");
//        jedis.zadd(randKey, 25, "tom");
//        jedis.zadd(randKey, 55, "lucy");
//        jedis.zadd(randKey, 95, "john");
//        jedis.zadd(randKey, 45, "jascii");
//        jedis.zadd(randKey, 65, "ben");
//        print(35, jedis.zcard(randKey));
//        print(36, jedis.zcount(randKey, 61,100));
//        print(37, jedis.zrange(randKey, 0, 100));
//        print(38,jedis.zrange(randKey, 1, 3 ));
//        for(Tuple tuple : jedis.zrangeByScoreWithScores(randKey,"60", "100")){
//            print(39, tuple.getElement() + " : "  +  String.valueOf(tuple.getScore()));
//        }
//
//        print(40, jedis.zrank(randKey, "john"));
//        print(41, jedis.zrevrank(randKey, "john"));
//
//
//        String setKey = "zSet";
//        jedis.zadd(setKey, 1, "a");
//        jedis.zadd(setKey, 1, "b");
//        jedis.zadd(setKey, 1, "c");
//        jedis.zadd(setKey, 1, "d");
//        jedis.zadd(setKey, 1, "e");
//        print(42, jedis.zlexcount(setKey,"-", "+"));
//        print(43, jedis.zlexcount(setKey, "[b", "[d"));
//        print(44,jedis.zrange(setKey, 0 ,10));
//        jedis.zremrangeByLex(setKey,"(c","+");
//        print(45,jedis.zrange(setKey, 0 ,10));


//        JedisPool pool = new JedisPool("localhost");
//        for(int i = 0; i < 10; ++i){
//            Jedis j = pool.getResource();
//            j.auth("root");
//            j.select(9);
//            print(46, j.get("pv"));
//            j.close();
//        }

//        User user = new User();
//        user.setName("xxName");
//        user.setPassword("xxPassword");
//        user.setSalt("haha");
//        user.setHeadUrl("a.png");
//        user.setId(1);
//        print(48, JSONObject.toJSONString(user));
//        jedis.set("user1", JSONObject.toJSONString(user));
//
//        String value = jedis.get("user1");
//        User user2 = JSON.parseObject(value, User.class);
//        print(49, user2);
//    }


}
