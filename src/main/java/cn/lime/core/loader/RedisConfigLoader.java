package cn.lime.core.loader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: RedisConfig
 * @Description: Redis配置
 * @Author: Lime
 * @Date: 2023/7/5 11:50
 */
@Configuration
public class RedisConfigLoader {

    //redis地址
    @Value("${spring.data.redis.host}")
    private String host;

    //redis端口号
    @Value("${spring.data.redis.port}")
    private int port;

    //redis密码
    @Value("${spring.data.redis.password}")
    private String password;

    //默认数据库
    //多个数据库集合
    @Value("${spring.data.redis.dbs}")
    private String dbListStr;

    private List<Integer> dbList;

    @Bean
    public Map<Integer, StringRedisTemplate> redisTemplateMap(){
        Map<Integer, StringRedisTemplate> redisTemplateMap = new HashMap<>();
        dbList = new ArrayList<>();
        for (String dbNumberStr : dbListStr.split(",",-1)){
            dbList.add(Integer.valueOf(dbNumberStr));
        }
        for (Integer db : dbList) {
            //存储多个RedisTemplate实例
            redisTemplateMap.put(db, redisTemplate(db));
        }
        return redisTemplateMap;
    }

    public LettuceConnectionFactory redisConnection(int db) {
        RedisStandaloneConfiguration server = new RedisStandaloneConfiguration();
        server.setHostName(host); // 指定地址
        server.setDatabase(db); // 指定数据库
        server.setPort(port); //指定端口
        server.setPassword(RedisPassword.of(password)); //指定密码
        LettuceConnectionFactory factory = new LettuceConnectionFactory(server);
        factory.afterPropertiesSet(); //刷新配置
        return factory;
    }

    //RedisTemplate模板
    public StringRedisTemplate redisTemplate(int db) {
        //为了开发方便，一般直接使用<String,Object>
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnection(db)); //设置连接
        //使用默认的String的序列化
        template.afterPropertiesSet();
        return template;
    }


}
