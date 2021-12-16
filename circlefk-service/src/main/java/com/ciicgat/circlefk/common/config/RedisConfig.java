package com.ciicgat.circlefk.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Objects;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);

    @Autowired
    private Environment env;

    /**
     * 在使用@Cacheable时，如果不指定key，则使用这个默认的key生成器生成的key
     *
     * @return
     */
    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate() {
        return getTemplate(redisConnectionFactory());
    }

    private RedisConnectionFactory redisConnectionFactory() {
        if (env.getActiveProfiles()[0].equals("dev")) {
            String host = env.getProperty("spring.redis.host");
            int port = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.port")));
            String password = env.getProperty("spring.redis.password");
            int timeout = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.timeout")));
            int database = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.database")));
            int min_idle = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.lettuce.pool.min-idle")));
            int max_idle = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.lettuce.pool.max-idle")));
            int max_active = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.lettuce.pool.max-active")));
            int max_wait = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.lettuce.pool.max-wait")));
            return connectionFactory(max_active, max_idle, min_idle, max_wait, host, password, timeout, port, database);
        } else {
            // 自己通过接口调redis ip端口
            // 或者可以存在服务器，读取文件
            String host = env.getProperty("spring.redis.host");
            int port = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.port")));
            String password = env.getProperty("spring.redis.password");
            int timeout = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.timeout")));
            int database = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.database")));
            int min_idle = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.lettuce.pool.min-idle")));
            int max_idle = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.lettuce.pool.max-idle")));
            int max_active = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.lettuce.pool.max-active")));
            int max_wait = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.redis.lettuce.pool.max-wait")));
            return connectionFactory(max_active, max_idle, min_idle, max_wait, host, password, timeout, port, database);
        }
    }


    /**
     * 创建连接
     *
     * @param maxActive
     * @param maxIdle
     * @param minIdle
     * @param maxWait
     * @param host
     * @param password
     * @param timeout
     * @param port
     * @param database
     * @return
     */
    private RedisConnectionFactory connectionFactory(Integer maxActive,
                                                     Integer maxIdle,
                                                     Integer minIdle,
                                                     Integer maxWait,
                                                     String host,
                                                     String password,
                                                     Integer timeout,
                                                     Integer port,
                                                     Integer database) {
        // 单点redis
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

        /**
         * 哨兵redis
         Set<String> setRedisNode = new HashSet<>();
         setRedisNode.add(HOST + ":" + PORT1);
         setRedisNode.add(HOST + ":" + PORT2);
         setRedisNode.add(HOST + ":" + PORT3);
         RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration("master",setRedisNode);
         */
        /**
         * 集群redis
         RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
         // 跨集群执行命令时要遵循的最大重定向数量
         redisConfig.setMaxRedirects(5);
         List<RedisNode> nodeList = new ArrayList<>();
         nodeList.add(new RedisNode(HOST, PORT));
         nodeList.add(new RedisNode(HOST, PORT));
         nodeList.add(new RedisNode(HOST, PORT));
         redisConfig.setClusterNodes(nodeList);
         */


        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWaitMillis(maxWait);
        LettuceClientConfiguration lettucePoolingConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig).shutdownTimeout(Duration.ofMillis(timeout)).build();
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration,
                lettucePoolingConfig);
        connectionFactory.afterPropertiesSet();
        LOGGER.info("redis: url:{}" , host + ":" + port);
        return connectionFactory;
    }

    /**
     * 创建 RedisTemplate 连接类型，此处为hash
     *
     * @param factory
     * @return
     */
    private RedisTemplate<Object, Object> getTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setValueSerializer(jackson2JsonRedisSerializer(new ObjectMapper()));
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer(new ObjectMapper()));

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 对value 进行序列化
     *
     * @param objectMapper
     * @return
     */
    private Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer(ObjectMapper objectMapper) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return jackson2JsonRedisSerializer;
    }
}
