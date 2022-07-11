package io.bodin.rest;

import io.bodin.rest.contract.Immutable;

import java.io.Serializable;
import java.util.Objects;

public class Header implements Serializable, Immutable {
    private final String name;
    private final String value;

    protected Header(){
        this(null, null);
    }
    public Header(String name, String value) {
        this.name = name;
        this.value = value;
    }

    protected Header(Header header){
        this.name = header.name;
        this.value = header.value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Object copy() {
        return new Header(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return Objects.equals(name, header.name) && Objects.equals(value, header.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
