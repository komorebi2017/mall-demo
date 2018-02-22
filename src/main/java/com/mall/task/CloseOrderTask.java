package com.mall.task;

import com.mall.common.Const;
import com.mall.service.IOrderService;
import com.mall.util.PropertiesUtil;
import com.mall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/2/11 18:32
 * @ Description:
 */
@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

   // @Scheduled(cron = "0 */1 * * * ?")  //每一分钟（每个一分钟的整数倍）
    public void closeOrderTaskV1(){
        log.info("关闭订单定时任务启动");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务结束");
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV2(){
        log.info("关闭订单定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty(PropertiesUtil.getProperty("lock.timeout"),"50000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));
        if ( setnxResult != null && setnxResult.intValue() == 1){
            /* 如果返回值是1， 代表设置成功， 获取锁 */
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);

        }else {
            log.info("没有获得分布式锁:{}",Const.REDIS_LOCK
            .CLOSE_ORDER_TASK_LOCK);
        }
        log.info("关闭订单定时任务启动");

    }

    /* 把锁设置有效期 */
    private void closeOrder(String lockName){
        RedisShardedPoolUtil.expire(lockName,50); /* 有效期50秒，防止死锁 */
        log.info("获取{}，ThreadName:{}",Const.REDIS_LOCK
        .CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        iOrderService.closeOrder(hour);
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{}，ThreadName:{}",Const.REDIS_LOCK
                .CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        log.info("===========================");


    }

}
