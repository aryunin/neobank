package com.aryunin.conveyor.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalSerializer extends JsonSerializer<BigDecimal> {
    @Value("${decimal-scaling.serialization}")
    private int scale;

    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(bigDecimal.setScale(scale, RoundingMode.HALF_UP));
    }
}
