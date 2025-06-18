package com.clover.salad.common.json;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FlexibleDateDeserializer extends JsonDeserializer<LocalDate> {
    private static final DateTimeFormatter[] formats = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"), DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("yyyy.MM.dd")};

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        for (DateTimeFormatter format : formats) {
            try {
                return LocalDate.parse(value, format);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new InvalidFormatException(p, "지원하지 않는 날짜 형식입니다.", value, LocalDate.class);
    }
}
