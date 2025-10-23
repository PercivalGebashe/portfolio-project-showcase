package io.github.PercivalGebashe.portfolio_project_showcase.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StringToMapConverter implements Converter<String, Map<String, String>> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<String, String> convert(String source) {
        try {
            return mapper.readValue(source, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON for Map<String, String>: " + source);
        }
    }
}

