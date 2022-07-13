package io.bodin.rest.model;

import io.bodin.rest.contract.Immutable;

import java.io.Serializable;
import java.time.Duration;

public class Entity<T> implements Immutable<Entity> {

    public static String APPLICATION_JSON = "application/json";
    public static String APPLICATION_XML = "application/xml";
    public static String TEXT_PLAIN = "text/plain";

    private final String contentType;
    private final T entity;

    public static final <T> Entity<T> None(){
      return new Entity<>(null, null);
    }

    public static <T> Entity<T> of(String contentType, T entity){
        return new Entity<>(contentType, entity);
    }
    public static <T> Entity<T> ofJson(T entity){
        return new Entity<>(APPLICATION_JSON, entity);
    }
    public static <T> Entity<T> ofXml(T entity){
        return new Entity<>(APPLICATION_XML, entity);
    }
    public static <T> Entity<T> ofPlain(T entity){
        return new Entity<>(TEXT_PLAIN, entity);
    }

    protected Entity(){
        this(null, null);
    }

    protected Entity(String contentType, T entity) {
        this.contentType = contentType;
        this.entity = entity;
    }

    protected Entity(Entity<T> o){
       this(o.contentType, o.entity);
    }

    public String getContentType() {
        return contentType;
    }

    public T getEntity() {
        return entity;
    }
}
