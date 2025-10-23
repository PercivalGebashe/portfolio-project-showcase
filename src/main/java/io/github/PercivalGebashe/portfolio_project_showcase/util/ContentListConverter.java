package io.github.PercivalGebashe.portfolio_project_showcase.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.PercivalGebashe.portfolio_project_showcase.dto.ProjectDTO;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

@Converter
public class ContentListConverter implements AttributeConverter<List<ProjectDTO.ContentItem>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<ProjectDTO.ContentItem> contentItems) {
        try {
            return contentItems == null ? null : objectMapper.writeValueAsString(contentItems);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting content list to JSON", e);
        }
    }

    @Override
    public List<ProjectDTO.ContentItem> convertToEntityAttribute(String  json) {
        try{
            return json == null ? null : objectMapper.readValue(json, new TypeReference<>(){});
        } catch (IOException e){
            throw new IllegalArgumentException("Error converting JSON to List", e);
        }
    }
}
