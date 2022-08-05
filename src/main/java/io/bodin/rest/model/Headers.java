package io.bodin.rest.model;

import io.bodin.rest.contract.Immutable;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Headers implements Serializable, Iterable<Header>, Immutable<Headers> {

    public static String CONTENT_TYPE = "Content-Type";

    private final List<Header> headers;

    public static final Headers None = new Headers();

    public static Builder builder(){
        return new Headers.Builder();
    }
    public static Builder withHeades(Headers headers){
        Builder result = new Builder();
        headers.forEach(h -> result.header(h.getName(), h.getValue()));
        return result;
    }
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
    public Iterator<Header> iterator() {
        return this.headers.iterator();
    }

    public Map<String, String> collapse() {
        Map<String, String> headers = new LinkedHashMap<>();
        for (Header h : this.headers) {
            headers.merge(h.getName(), h.getValue(), (le, ri) -> le + "," + ri);
        }
        return headers;
    }

    public static class Builder {

        private List<Header> headers = new ArrayList<>();

        public Builder header(String name, String value) {
            this.headers.add(new Header(name, value));
            return this;
        }
        public Builder remove(String name) {
            this.headers.stream().filter(h -> !name.equalsIgnoreCase(h.getName())).collect(Collectors.toList());
            return this;
        }

        public Builder replace(String name, String value) {
            this.remove(name).header(name, value);
            return this;
        }

        public Headers build() {
            return new Headers(headers);
        }
    }

    @Override
    public String toString() {
        return "Headers{" +
                "headers=" + headers +
                '}';
    }
}
