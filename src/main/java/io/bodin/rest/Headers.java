package io.bodin.rest;

import io.bodin.rest.contract.Immutable;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

public class Headers implements Serializable, Iterable<Header>, Immutable<Headers> {
    private final List<Header> headers;

    public static Headers None = new Headers();
    public static Builder withHeader(String name, String value){
        return new Builder().header(name, value);
    }

    protected Headers(){
        this(Collections.emptyList());
    }
    protected Headers(Headers other) {
        this(new ArrayList<>(other.headers));
    }

    protected Headers(List<Header> headers) {
        this.headers = headers;
    }

    @Override
    public Headers copy() {
        return new Headers(this);
    }

    @Override
    public Iterator<Header> iterator() {
        return this.headers.iterator();
    }

    public static class Builder {

        private List<Header> headers = new ArrayList<>();

        public Builder header(String name, String... value) {
            Arrays.asList(value).forEach(v -> this.headers.add(new Header(name, v)));
            return this;
        }

        public Headers build() {
            return new Headers(headers);
        }
    }
}
