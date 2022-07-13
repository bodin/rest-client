package io.bodin.rest.engine.simple.handlers;

import java.io.IOException;
import java.io.InputStream;

public class ByteContentHandler implements ContentHandler {

    @Override
    public <T> boolean isSupported(String contentType, Class<T> type) {
        return type.equals(byte[].class);
    }

    public <T> T parse(InputStream in, Class<T> type) throws IOException {
        @SuppressWarnings("unchecked")
        T result = (T)in.readAllBytes();
        return result;
    }
}
