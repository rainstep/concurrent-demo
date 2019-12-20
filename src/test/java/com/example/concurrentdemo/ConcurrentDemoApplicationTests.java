package com.example.concurrentdemo;

import com.example.concurrentdemo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConcurrentDemoApplicationTests {
    Logger logger = LoggerFactory.getLogger("Test");

    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testAddAmount() throws InterruptedException {
        int threadCount = 10;
        int userId = 1;
        double addAmount = 100.00;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        double startAmount = userService.getAmountByUserId(userId);
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                userService.addMountByUserId(userId, addAmount); // 线程不安全，并发可能导致实际总增加金额变少
                userService.saveAddMountByUserId(userId, addAmount);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        watch.stop();
        double endAmount = userService.getAmountByUserId(userId);
        logger.info("总线程数: {}", threadCount);
        logger.info("每次线程增加金额: {}", addAmount);
        logger.info("总消耗时间: {}秒", watch.getTotalTimeSeconds());
        double expectedTotalAddAmount = threadCount * addAmount;
        logger.info("开始金额: {}, 预期总增加金额: {}, 预期最后金额: {}", startAmount, expectedTotalAddAmount, startAmount + expectedTotalAddAmount);
        logger.info("开始金额: {}, 实际总增加金额: {}, 实际最后金额: {}", startAmount, endAmount - startAmount, endAmount);
    }

    @Test
    public void testSubtractAmount() throws InterruptedException {
        int threadCount = 5;
        int userId = 1;
        double subtractAmount = 300.00;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        double startAmount = userService.getAmountByUserId(userId);
        StopWatch watch = new StopWatch();
        watch.start();
        double expectedLastAmount = startAmount;
        double expectedSubtractAmount = 0;
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                userService.subtractMountByUserId(userId, subtractAmount); // 线程不安全，并发可能导实际最后金额变为负数
//                userService.saveSubtractMountByUserId(userId, subtractAmount);
                countDownLatch.countDown();
            }).start();
            if (expectedLastAmount >= subtractAmount) {
                expectedLastAmount -= subtractAmount;
                expectedSubtractAmount += subtractAmount;
            }
        }
        countDownLatch.await();
        watch.stop();
        double endAmount = userService.getAmountByUserId(userId);
        logger.info("总线程数: {}", threadCount);
        logger.info("每次线程扣除金额: {}", subtractAmount);
        logger.info("总消耗时间: {}秒", watch.getTotalTimeSeconds());
        logger.info("开始金额: {}, 预期总扣除金额: {}, 预期最后金额: {}", startAmount, expectedSubtractAmount, expectedLastAmount);
        logger.info("开始金额: {}, 实际总扣除金额: {}, 实际最后金额: {}", startAmount, startAmount - endAmount, endAmount);
    }

    static int count = 0;

    @Test
    public void testList() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            count = 0;
            test();
        }
    }

    @Test
    public void test() throws InterruptedException {
        int threadCount = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        AtomicInteger atomicCount = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                atomicCount.getAndIncrement();
                countDownLatch.countDown();

            }).start();
        }
        countDownLatch.await();
        if (count != threadCount) {
            logger.info("预期count: {}, 实际count: {}", threadCount, count);
        }
        if (atomicCount.intValue() != threadCount) {
            logger.info("预期atomicCount: {}, 实际atomicCount: {}", threadCount, atomicCount);
        }
    }

}
