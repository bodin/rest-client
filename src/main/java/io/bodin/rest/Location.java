package io.bodin.rest;

import io.bodin.rest.contract.Immutable;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Stream;

public class Location implements Serializable, Immutable<Location> {
    public static Location Root = new Location();

    private final List<String> path;
    private final Map<String, List<String>> params;
    private final String hash;

    public static Builder withRoot(){
        return new Builder();
    }

    public static Builder withPath(String ... s){
        return new Builder().path(s);
    }

    public static Builder withParam(String n, String ... v){
        return new Builder().param(n, v);
    }

    public static Builder withHash(String h){
        return new Builder().hash(h);
    }

    protected Location(){
        this(Collections.emptyList(), Collections.emptyMap(), null);
    }
    protected Location(Location l){
        this(new ArrayList<>(l.path), Utils.copyLinked(l.params), l.hash);
    }

    public Location(List<String> path, Map<String, List<String>> params, String hash) {
        this.path = path;
        this.params = params;
        this.hash = hash;
    }
    public URL toURL(URL base) throws MalformedURLException {
        StringBuilder sb = new StringBuilder(base.toString());
        if(!this.path.isEmpty()){
            //TODO - this is not 100% right
            sb.append("/").append(String.join("/", this.path));
        }
        if(!this.params.isEmpty()){
            sb.append("?");
            this.params.entrySet().forEach(e -> {
                e.getValue().forEach(v -> {
                    sb.append(e.getKey()).append('=').append(encode(v)).append('&');
                });
            });
            if(sb.charAt(sb.length()-1) == '&'){
                sb.setLength(sb.length()-1);
            }
        }
        if(this.hash != null){
            sb.append('#').append(encode(this.hash));
        }
        return URI.create(sb.toString()).toURL();
    }
    private String encode(String in){
        return URLEncoder.encode(in, Charset.defaultCharset());
    }
    @Override
    public Location copy() {
        return new Location(this);
    }

    public static class Builder {
        private List<String> path = new ArrayList<>();
        private Map<String, List<String>> params = new LinkedHashMap<>();
        private String hash;

        public Builder path(String ... path) {
            this.path.addAll(Arrays.asList(path));
            return this;
        }

        public Builder param(String name, String ... values){
            this.params.merge(name, Arrays.asList(values), (o, t) -> Stream.concat(o.stream(), t.stream()).toList());
            return this;
        }

        public Builder hash(String hash) {
            this.hash = hash;
            return this;
        }

        public Location build() {
            return new Location(path, params, hash);
        }
    }
}
