package io.bodin.rest.engine.simple.handlers;

import com.google.gson.Gson;
import io.bodin.rest.model.Entity;

import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonContentHandler implements ContentHandler {

    @Override
    public <T> boolean isSupported(String contentType, Class<T> type) {
        return Entity.APPLICATION_JSON.equalsIgnoreCase(contentType);
    }

    @Override
    public <T> T parse(InputStream in, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(new InputStreamReader(in), type);
    }
}
