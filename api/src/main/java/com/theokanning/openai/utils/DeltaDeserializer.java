package com.theokanning.openai.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class DeltaDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);
        if (!node.has("content")) {
            return null;
        }
        JsonNode content = node.get("content");

        return StreamSupport
                .stream(content.spliterator(), false)
                .map(jsonNode -> jsonNode.get("text"))
                .map(jsonNode -> jsonNode.get("value").asText())
                .collect(Collectors.joining());
    }
}
