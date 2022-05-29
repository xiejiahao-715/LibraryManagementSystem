package com.xjh.library.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

// Long类型的序列化处理
public class LongSerializer extends JsonSerializer<Long> {

    public static final LongSerializer instance = new LongSerializer();

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // js中Number精度是16位(雪花ID是19位的)，所以JS的Number数据类型导致的精度丢失
        String valueStr = value.toString();
        if(valueStr.length() <= 16){
            // 正常长度则正常序列化
            gen.writeNumber(value);
        }else{
            // 序列化时将过长的long类型转为字符串
            gen.writeString(valueStr);
        }
    }
}
