package com.clf.cloud.userserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServerApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        //要切割的字符串
        String   s   = "123.jpg,113.jpg,121.jpg,122.jpg,131.jpg";
        String   sub =   "";
        System.out.println("编译前："+s);
        //调用方法
        sub = s.replaceAll( ",113.jpg" + "|" +"113.jpg,|113.jpg","");//.replaceAll( ",122.jpg|122.jpg,","");
        System.out.println("编译后："+sub);
    }

}
