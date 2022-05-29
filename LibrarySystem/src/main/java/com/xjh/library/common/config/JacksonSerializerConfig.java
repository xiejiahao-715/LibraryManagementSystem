package com.xjh.library.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.xjh.library.common.utils.LongSerializer;
import com.xjh.library.common.utils.StringTrimModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// 序列化器jackson的配置类
@Configuration
public class JacksonSerializerConfig {
    /** 默认日期时间格式 */
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** 默认日期格式 */
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /** 默认时间格式 */
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();

        // 默认不序列值为NULL的字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 反序列化时遇到未知的字段不报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 添加解析java8时间类型的模块
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 添加时间类型的序列化支持
        javaTimeModule.addSerializer(LocalDateTime.class,new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class,new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class,new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        // 添加时间类型的反序列化支持
        javaTimeModule.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class,new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class,new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

        // 通用的简单模块配置
        SimpleModule simpleModule = new SimpleModule();
        // 解决Long或long类型结果序列化后精度丢失的问题，将Long或long序列化为字符串就行了
        simpleModule.addSerializer(Long.class, LongSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, LongSerializer.instance);

        objectMapper
                // 注册通用的简单模块
                .registerModule(simpleModule)
                // 注册处理java8新日期类型的模块
                .registerModule(javaTimeModule)
                .registerModule(new ParameterNamesModule())
                // 添加自定义的StringTrimModule模块
                .registerModule(new StringTrimModule());
        return objectMapper;
    }
}
