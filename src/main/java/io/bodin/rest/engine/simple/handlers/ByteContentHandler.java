package io.bodin.rest.engine.simple.handlers;

import io.bodin.rest.model.Entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteContentHandler implements ContentHandler {

    @Override
    public <T> boolean isSupportedParse(String contentType, Class<T> type) {
        return type.equals(byte[].class);
    }

    @Override
    public <T> boolean isSupportedWrite(Entity<T> entity) {
        return entity.getContentType().equalsIgnoreCase(Entity.TEXT_PLAIN);
    }

    @Override
    public void write(OutputStream out, Entity<?> e) throws IOException {
        if(e.getEntity() == null) return;
        out.write(e.getEntity().toString().getBytes());
    }

    public <T> T parse(InputStream in, Class<T> type) throws IOException {
        @SuppressWarnings("unchecked")
        T result = (T)in.readAllBytes();
        return result;
    }


}
