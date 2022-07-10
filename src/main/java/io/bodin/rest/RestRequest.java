package io.bodin.rest;

import io.bodin.rest.contract.Immutable;

public class RestRequest<SEND, READ> implements Immutable<RestRequest> {
    private final Method method;
    private final Location location;
    private final Headers headers;
    private final Options options;
    private final SEND entity;
    private final Class<READ> responseType;

    public static <SEND, READ> RestRequest.Builder<SEND, READ> builder(){
        return new Builder<>();
    }
    public static RestRequest.Builder withGet(){
        return builder().method(Method.GET);
    }
    public static RestRequest.Builder withPost(){
        return builder().method(Method.POST);
    }
    public static RestRequest.Builder withPut(){
        return builder().method(Method.PUT);
    }
    public static RestRequest.Builder withDelete(){
        return builder().method(Method.DELETE);
    }
    public static RestRequest.Builder withOptions(){
        return builder().method(Method.OPTIONS);
    }
    public static RestRequest.Builder withPatch(){
        return builder().method(Method.PATCH);
    }

    protected RestRequest(Method method, Location location, Headers headers, Options options, SEND entity, Class<READ> responseType) {
        this.method = method;
        this.location = location;
        this.headers = headers;
        this.options = options;
        this.entity = entity;
        this.responseType = responseType;
    }

    public Method getMethod() {
        return method;
    }

    public Location getLocation() {
        return location;
    }

    public Headers getHeaders() {
        return headers;
    }

    public Options getOptions() {
        return options;
    }

    public SEND getEntity() {
        return entity;
    }

    public Class<READ> getResponseType() {
        return responseType;
    }

    @Override
    public RestRequest<SEND, READ> copy() {
        return new RestRequest<>(method, location, headers, options, entity, responseType);
    }

    public static class Builder<SEND, READ> {
        private Method method;
        private Location location;
        private Headers headers;
        private Options options;
        private SEND entity;
        private Class<READ> responseType;
        public Builder<SEND, READ> method(Method method) {
            this.method = method;
            return this;
        }
        public Builder<SEND, READ> location(Location location) {
            this.location = location;
            return this;
        }
        public Builder<SEND, READ>headers(Headers headers) {
            this.headers = headers;
            return this;
        }
        public Builder<SEND, READ> options(Options options) {
            this.options = options;
            return this;
        }
        public Builder<SEND, READ> entity(SEND entity) {
            this.entity = entity;
            return this;
        }
        public Builder<SEND, READ> responseType(Class<READ> responseType) {
            this.responseType = responseType;
            return this;
        }
        public RestRequest<SEND, READ> build() {
            return new RestRequest<>(method, location, headers, options, entity, responseType);
        }
    }
}
