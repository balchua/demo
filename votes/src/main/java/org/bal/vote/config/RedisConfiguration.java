package org.bal.vote.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
public class RedisConfiguration {

    @Value("${redis-server.host}")
    private String redisHost;

    @Value("${redis-server.port}")
    private int redisPort;

    @Value("${redis-server.password}")
    private String redisPassword;


    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPassword));

        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplate() {
        RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setValueSerializer(new GenericToStringSerializer<Integer>(Integer.class));
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

}