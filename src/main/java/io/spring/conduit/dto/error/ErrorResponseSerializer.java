package io.spring.conduit.dto.error;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorResponseSerializer extends JsonSerializer<ErrorResponse> {

    /**
     *
     * @param errorResponse
     * @param jsonGenerator
     * @param serializerProvider
     * @throws IOException
     *
     * Result should be like
     * {
     *     "errors": {
     *         "email": [
     *             "duplicated email"
     *         ],
     *         "username": [
     *             "duplicated username"
     *         ]
     *     }
     * }
     */

    @Override
    public void serialize(ErrorResponse errorResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Map<String, List<String>> json = new HashMap<>();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldStart("errors");
        for(ErrorResponseField fieldError : errorResponse.getErrors()){
            if(!json.containsKey(fieldError.getField())){
                json.put(fieldError.getField(),new ArrayList<>());
            }
            json.get(fieldError.getField()).add(fieldError.getMessage());
        }
        for(Map.Entry<String, List<String>> pair : json.entrySet()){
            jsonGenerator.writeArrayFieldStart(pair.getKey());
            pair.getValue().forEach( each -> {
                try {
                    jsonGenerator.writeString(each);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            jsonGenerator.writeEndArray();
        }

        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
    }
}
