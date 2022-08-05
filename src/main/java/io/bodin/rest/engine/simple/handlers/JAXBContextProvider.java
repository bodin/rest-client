package io.bodin.rest.engine.simple.handlers;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

@FunctionalInterface
public interface JAXBContextProvider {
    JAXBContext provide(Class<?> type) throws JAXBException;
}
