package cn.lime.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName: MainApplication
 * @Description: TODO 描述类的功能
 * @Author: riang
 * @Date: 2026/4/20 19:15
 */
/**
 * 启动程序
 *
 * @author caixh
 */
@SpringBootApplication
@MapperScan({"cn.lime.core.mapper"})
@EnableScheduling
@EnableAsync
public class MainApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(MainApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}