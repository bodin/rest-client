package io.bodin.rest;

import io.bodin.rest.contract.Immutable;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

public class Headers implements Serializable, Iterable<Map.Entry<String, List<String>>>, Immutable<Headers> {
    private final Map<String, List<String>> headers;

    public static Headers None = new Headers();
    public static Builder withHeader(String name, String value){
        return new Builder().header(name, value);
    }

    protected Headers(){
        this(Collections.emptyMap());
    }
    protected Headers(Headers other) {
        this(Utils.copyLinked(other.headers));
    }

    protected Headers(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    @Override
    public Headers copy() {
        return new Headers(this);
    }

    public  Map<String, List<String>> asMap() {
        return new Headers(this).headers;
    }

    @Override
    public Iterator<Map.Entry<String, List<String>>> iterator() {
        return this.headers.entrySet().iterator();
    }

    public static class Builder {

        private Map<String, List<String>> headers = new LinkedHashMap<>();

        public Builder header(String name, String... value) {
            this.headers.merge(name, Arrays.asList(value), (o, t) -> Stream.concat(o.stream(), t.stream()).toList());
            return this;
        }

        public Headers build() {
            return new Headers(headers);
        }
    }
}
