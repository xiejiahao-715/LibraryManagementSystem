package com.xjh.library.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.paramnames.PackageVersion;

import java.io.IOException;

// 添加有关序列化操作时去除字符串多余空格的反序列化模块
public class StringTrimModule extends SimpleModule {

    public StringTrimModule(){
        super(PackageVersion.VERSION);
        // 添加反序列化
        addDeserializer(String.class, new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
                String value = parser.getValueAsString();
                if(value != null){
                    value = value.trim();
                    if(value.equals("")){
                        value = null;
                    }
                }
                return value;
            }
        });
        // 添加序列化时操作
        addSerializer(String.class, new JsonSerializer<String>() {
            // 文档：Value to serialize; can not be null.
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.trim());
            }
        });
    }

}
