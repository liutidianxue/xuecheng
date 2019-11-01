package com.xuecheng.filesystem.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author hewei
 * @date 2019/11/1 - 19:54
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestUpload {

    @Test
    public void testStringUtils(){
        String s = "哈哈谁打的.txt";
        String s1 = StringUtils.substringAfterLast(s, ".");
        System.out.println(s1);
    }
}
