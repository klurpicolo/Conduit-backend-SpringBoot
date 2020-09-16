package io.spring.conduit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.conduit.model.TestModel;

public class Test {

    public static void main (String[] args) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        System.out.println(om.readValue("{\n" +
                "  \"article\": {\n" +
                "    \"title\": \"title2\",\n" +
                "    \"description\": \"descriptiona\",\n" +
                "    \"body\": \"bodys\",\n" +
                "    \"tagList\": [\n" +
                "      \"tag1\",\"tagg2\"\n" +
                "    ]\n" +
                "  }\n" +
                "}", TestModel.class));
    }
}
