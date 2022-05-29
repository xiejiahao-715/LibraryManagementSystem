package com.xjh.library.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.NettyCustomizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
        // json序列化配置
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = getJackson2JsonRedisSerializer();

        template.setConnectionFactory(redisConnectionFactory);
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash-key采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value的序列化采用Jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化也采用Jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    // json序列化Redis的配置
    private Jackson2JsonRedisSerializer<Object> getJackson2JsonRedisSerializer(){
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }


    // 配置lettuce连接的心跳设置，防止出现远程连接超时的情况
    @Bean
    public ClientResources clientResources(){

        NettyCustomizer nettyCustomizer = new NettyCustomizer() {
            @Override
            public void afterChannelInitialized(Channel channel) {
                channel.pipeline().addLast(
                        //此处事件必须小于超时时间
                        new IdleStateHandler(40, 0, 0));
                channel.pipeline().addLast(new ChannelDuplexHandler() {
                    @Override
                    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                        if (evt instanceof IdleStateEvent) {
                            ctx.disconnect();
                        }
                    }
                });
            }
            @Override
            public void afterBootstrapInitialized(Bootstrap bootstrap) {

            }
        };
        return ClientResources.builder().nettyCustomizer(nettyCustomizer).build();
    }

}
