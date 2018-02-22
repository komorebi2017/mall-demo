package com.mall.common;

import com.mall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/2/8 7:53
 * @ Description:
 */
public class RedisShardedPool {


    private static ShardedJedisPool pool;    // Sharded Jedis连接池

    /* 控制Jedis连接池里面和RedisServer最大的连接数*/
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));

    /* 在这个连接池中最多有多少个状态为Idle的Jedis实例
     * Jedis连接池里面是一些Jedis实例，
     * Idle是空闲，也就是Jedis连接池中有一些空闲的Jedis实例
     * 如果想用的话立刻就能拿出来 */
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));  // 在JedisPool中最大的idle（空闲的）状态的Jedis实例的个数

    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));  // 在JedisPool中最小的idle状态的Jedis实例的个数

    /* 程序在从JedisPool里面Borrow一个Jedis实例，从里面拿一个实例和Redis通信的时候，是否要进行一个test验证一下
     * 如果这个实例是没有损坏的，验证的结果会返回True，如果验证的结果有问题返回false
     * 是个bool类型，如果声明为True，说明只有验证了这个实例OK才会得到Jedis实例
     * 也就说明了把这个值声明为True，保证每次拿到的实例都是可用的*/

    /* 补充说明：
     * testOnBorrow和testOnReturn时，默认的值都是false
     * 当并发非常高的时候，而且在return放回连接池的时候，是有一个判断，把它放回returnResource，还是returnBrokenResource
     * 所以默认值设为false，当发生了异常，就通过returnBrokenResource，放入BrokenResource里面
     * 也就是说在返回的时候就不需要验证了，就可以提高连接的效率
     * */
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));  //在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。

    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));  //在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");

    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");

    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));


    /* 是一个静态方法，后面再加一个静态块调用这个方法，这个方法只会被调用一次private*/
    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        /* 连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true */
        config.setBlockWhenExhausted(true);

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip,redis1Port,1000*2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip,redis2Port,1000*2);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        /* redis提供两个分片策略，MURMUR_HASH和MD5，默认的是MURMUR_HASH，对应的是一致性算法*/
        pool = new ShardedJedisPool(config,jedisShardInfoList, Hashing.MURMUR_HASH,Sharded.DEFAULT_KEY_TAG_PATTERN);






    }

    static {
        initPool();
    }

    /* 拿到Jedis实例 */
    public static ShardedJedis getJedis(){
        return pool.getResource();
    }

    public static void returnResource(ShardedJedis jedis){
        pool.returnResource(jedis);

    }

    public static void returnBrokenResource(ShardedJedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args){
        ShardedJedis jedis = pool.getResource();

        for (int i=0; i<10; i++){
            jedis.set("key"+i,"value"+i);
        }
        returnResource(jedis);
       // pool.destroy();  //临时调用，销毁连接池中的所有连接
        System.out.println("program is end");
    }


}
