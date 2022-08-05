package io.bodin.rest.engine.simple.handlers;

import io.bodin.rest.model.Entity;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class XMLContentHandler implements ContentHandler {
    private final JAXBContextProvider contextProvider;
    public XMLContentHandler() {
        this(t -> JAXBContext.newInstance(t));
    }

    public XMLContentHandler(JAXBContextProvider contextProvider) {
        this.contextProvider = contextProvider;
    }

    @Override
    public <T> boolean isSupportedParse(String contentType, Class<T> type) {
        return Entity.APPLICATION_XML.equalsIgnoreCase(contentType);
    }

    @Override
    public <T> boolean isSupportedWrite(Entity<T> entity) {
        return Entity.APPLICATION_XML.equalsIgnoreCase(entity.getContentType());
    }

    @Override
    public void write(OutputStream out, Entity<?> entity) throws IOException {
        if(entity.getEntity() == null) return;
        try {
            JAXBContext jaxb = this.contextProvider.provide(entity.getEntity().getClass());
            Marshaller marshaller = jaxb.createMarshaller();
            marshaller.marshal(entity.getEntity(), out);
        }catch(Exception e){
            throw new IOException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T parse(InputStream in, Class<T> type) throws IOException {
        try {
            JAXBContext jaxb = this.contextProvider.provide(type);
            Unmarshaller unmarshaller = jaxb.createUnmarshaller();

            @SuppressWarnings("unchecked")
            T result = (T) unmarshaller.unmarshal(in);

            return result;
        }catch(Exception e){
            throw new IOException(e.getMessage(), e);
        }
    }
}
