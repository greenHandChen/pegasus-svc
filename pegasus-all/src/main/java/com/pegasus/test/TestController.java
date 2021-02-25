package com.pegasus.test;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "TEST")
@RestController("TEST.v1")
@RequestMapping("/v1/test")
@Slf4j
public class TestController {

    public static final String RESET_FREQUENCY_REDIS_SCRIPT = new StringBuilder()
            .append(" redis.call('del',KEYS[1]) ")
            .append(" return redis.call('incrBy',KEYS[1],ARGV[1]) ")
            .toString();


    @ApiOperation(value = "TEST-REDIS")
    @GetMapping("/test-redis-inc")
    public void testRedisInc() {

        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
//                redisHelper.delKey("test-redis-inc");
//                Long aLong = redisHelper.strIncrement("test-redis-inc", 1L);
//                log.info("Thread-{},result-{}", Thread.currentThread().getName(), aLong);

                RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

                DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
                redisScript.setResultType(Long.class);
                redisScript.setScriptText(RESET_FREQUENCY_REDIS_SCRIPT);

                List<String> keys = new ArrayList<>();
                keys.add("test-redis-inc");

                Long execute = redisTemplate.execute(redisScript, keys, "1");

                log.info("Thread-{},result-{}", Thread.currentThread().getName(), execute);

            }).start();
        }

    }

}
