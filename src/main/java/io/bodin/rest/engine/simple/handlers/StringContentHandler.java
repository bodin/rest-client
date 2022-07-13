package io.bodin.rest.engine.simple.handlers;

import io.bodin.rest.model.Entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class StringContentHandler extends ByteContentHandler {

    @Override
    public <T> boolean isSupportedParse(String contentType, Class<T> type) {
        return type.equals(String.class);
    }

    public <T> T parse(InputStream in, Class<T> type) throws IOException {
        @SuppressWarnings("unchecked")
        T result = (T)new String(in.readAllBytes(), Charset.defaultCharset());

        return result;
    }
}
