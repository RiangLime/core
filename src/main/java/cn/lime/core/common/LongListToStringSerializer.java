package cn.lime.core.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName: LongListToStringSerializer
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/21 16:48
 */
public class LongListToStringSerializer extends JsonSerializer<List<Long>> {
    @Override
    public void serialize(List<Long> value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartArray();
        for (Long longValue : value) {
            gen.writeString(longValue.toString());
        }
        gen.writeEndArray();
    }
}
