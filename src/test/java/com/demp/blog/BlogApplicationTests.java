package com.demp.blog;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class BlogApplicationTests {
    @Autowired
    private DruidDataSource dataSource;

    @Test
    void contextLoads() {
        System.out.println(dataSource);
    }

}
