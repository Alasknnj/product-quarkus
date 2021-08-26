package br.com.compasso.desafio.service.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;

@ApplicationScoped
public class JsonBuilder {

    public String toJson(Object object) throws IOException {
        return new ObjectMapper().writeValueAsString(object);
    }

    public <T> T fromJsonFile(String path, Class clazz) throws IOException {
        return (T) new ObjectMapper().readValue(new File(path), clazz);
    }
}
