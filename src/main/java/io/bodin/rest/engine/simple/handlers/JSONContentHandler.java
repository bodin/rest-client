package io.bodin.rest.engine.simple.handlers;

import com.google.gson.Gson;
import io.bodin.rest.model.Entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class JSONContentHandler implements ContentHandler {
    private final Gson gson;

    public JSONContentHandler() {
        this(new Gson());
    }

    public JSONContentHandler(Gson gson) {
        this.gson = gson;
    }

    @Override
    public <T> boolean isSupportedParse(String contentType, Class<T> type) {
        return Entity.APPLICATION_JSON.equalsIgnoreCase(contentType);
    }

    @Override
    public <T> boolean isSupportedWrite(Entity<T> entity) {
        return Entity.APPLICATION_JSON.equalsIgnoreCase(entity.getContentType());
    }

    @Override
    public void write(OutputStream out, Entity<?> entity) throws IOException {
        if(entity.getEntity() == null) return;
        out.write(this.gson.toJson(entity.getEntity()).getBytes());
    }

    @Override
    public <T> T parse(InputStream in, Class<T> type) {
        return this.gson.fromJson(new InputStreamReader(in), type);
    }

}
