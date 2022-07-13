package io.bodin.rest.engine.simple.handlers;

import io.bodin.rest.model.Entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ContentHandler {
    <T> boolean isSupportedWrite(Entity<T> entity);
    void write(OutputStream out, Entity<?> entity) throws IOException;

    <T> boolean isSupportedParse(String contentType, Class<T> type);
    <T> T parse(InputStream in, Class<T> type) throws IOException;
}
