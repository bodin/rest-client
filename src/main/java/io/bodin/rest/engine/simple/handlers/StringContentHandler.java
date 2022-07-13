package io.bodin.rest.engine.simple.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class StringContentHandler implements ContentHandler {

    @Override
    public <T> boolean isSupported(String contentType, Class<T> type) {
        return type.equals(String.class);
    }

    public <T> T parse(InputStream in, Class<T> type) throws IOException {
        @SuppressWarnings("unchecked")
        T result = (T)new String(in.readAllBytes(), Charset.defaultCharset());

        return result;
    }
}
