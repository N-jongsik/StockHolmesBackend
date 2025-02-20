package com.example.wms.infrastructure.config;

import com.example.wms.user.application.domain.LogoutAccessToken;
import com.example.wms.user.application.domain.RefreshToken;
import com.example.wms.user.application.domain.UserInfo;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Slf4j
@EnableCaching
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @PostConstruct
    public void init() {
        log.info("=== Redis Configuration Initializing ===");
        log.info("Spring Boot Version: {}", SpringBootVersion.getVersion());
        log.info("Redis Host from @Value: {}", host);
        log.info("Redis Port from @Value: {}", port);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        log.info("=== Creating Redis Connection Factory ===");
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        log.info("Setting Redis host: {}", host);
        redisConfiguration.setHostName(host);
        redisConfiguration.setPort(port);

        // 비밀번호가 설정되어 있지 않은 경우 설정하지 않음
        if (password != null && !password.isEmpty()) {
            redisConfiguration.setPassword(password);
        }

        return new LettuceConnectionFactory(redisConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, LogoutAccessToken> logoutAccessTokenRedisTemplate() {
        RedisTemplate<String, LogoutAccessToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(LogoutAccessToken.class));

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(LogoutAccessToken.class));

        return redisTemplate;
    }


    @Bean
    public RedisTemplate<String, RefreshToken> refreshTokenRedisTemplate() {
        RedisTemplate<String, RefreshToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(RefreshToken.class));

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RefreshToken.class));

        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, UserInfo> userInfoRedisTemplate() {
        RedisTemplate<String, UserInfo> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(UserInfo.class));

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(UserInfo.class));

        return redisTemplate;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1))) // 캐시 TTL 1시간
                .build();
    }
}
