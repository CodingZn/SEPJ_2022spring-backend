package com.example.demo.bean.jsonUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public abstract class BasicSerializer<T> extends JsonSerializer {

    abstract Object getId(T bean);
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if(o == null)
            jsonGenerator.writeNull();
        String id = String.valueOf(getId( (T) o));
        if(id == null)
            jsonGenerator.writeNull();
        else
            jsonGenerator.writeString(id);
    }
}
