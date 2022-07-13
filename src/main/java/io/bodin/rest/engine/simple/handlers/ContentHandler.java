package io.bodin.rest.engine.simple.handlers;

import java.io.IOException;
import java.io.InputStream;

public interface ContentHandler {
    <T> boolean isSupported(String contentType, Class<T> type);
    <T> T parse(InputStream in, Class<T> type) throws IOException;
}
