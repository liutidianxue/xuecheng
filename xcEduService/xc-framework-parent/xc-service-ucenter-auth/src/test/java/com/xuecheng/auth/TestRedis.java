package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.bouncycastle.jcajce.provider.digest.MD2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hewei
 * @date 2019/11/10 - 15:30
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    //创建jwt令牌
    @Test
    public void testRedis(){
        //定义key
        String key = "user_token:d7136d97-9cb5-485c-ae08-15b22dc3b0df";
        //定义value
        Map<String,String> value = new HashMap<>();
        value.put("jwt", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU3MzM2OTI2MiwianRpIjoiZDcxMzZkOTctOWNiNS00ODVjLWFlMDgtMTViMjJkYzNiMGRmIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.O6l8EqgaEhhoeJNTqw-ufKaEKX6O0TQx5J0RQ7tbpc-q_sg1vnMAwZsnAZTHtivqP93AAhAzW1k4YsMulfBG2iqbxm43XAURfS2l8zVSc74Jl_Aw0sZjj5grzbyu5nM02lRfSf7jSJCdHh5Gw_8GFN5dlVKwepHS6XLvuB7lTFUqljdxb2ou1uNx_CMbwJ4kqOsxcNSORVnNf0vX9sLoMO4S2vRqSvBr-6VICMKw6z8_j4iIxGYM22zPJQ9XbTbHARxjqoAHioyq7g3t84WZmByL-1bg8Cjd2jDombNj_25iXInw42TBjLfPO2H7e6Z0YbKnElDlPFRT10TImH0XPA");
        value.put("refresh_token", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJhdGkiOiJkNzEzNmQ5Ny05Y2I1LTQ4NWMtYWUwOC0xNWIyMmRjM2IwZGYiLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU3MzM2OTI2MiwianRpIjoiY2YxMzQ2MzQtYmVkNC00MTRlLTk2MjEtNjE3M2E0YjY5NGJiIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.cJDq-m3z8QlTJbM2ZQy6bq1S7l6MasANUDUkKnrL_nRL6wXz-lcL9xWMtD00HPSZpD7AvWCZ6dAGLf62jpjBplCTW9EFm5u8ak8QlfFasgPl3d422-sHylefwami54TkRynu17c3iqjKgdYUhFtBSBcKvj3nmMA_muSFzJjcYobcbeb14VMXN5o0-3NtZpySUanMhL7ElsCkNGhGVg2hlbpqN3uucfwThTYA3-7PNVR1hEtPvMm5skmIofzbM1_PFY-48D4NUAVqgNXYc2t4sDBkajsSe-L-zbJ9joGNVgsYe9aKl3PFT0KcXZMJxux_bZn4djDfjYbqnt-bxTNPKQ");
        String jsonString = JSON.toJSONString(value);
        //校验key是否存在，如果不存在则返回-2
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        System.out.println(expire);
        //存储数据
        stringRedisTemplate.boundValueOps(key).set(jsonString,60,TimeUnit.SECONDS);
        //获取数据
        String s = stringRedisTemplate.opsForValue().get(key);
        System.out.println(s);

    }


}
