package io.bodin.rest.model;

import io.bodin.rest.contract.Immutable;

public class RestRequest<SEND, READ> implements Immutable<RestRequest> {
    private final Method method;
    private final Location location;
    private final Headers headers;
    private final Options options;
    private final Entity<SEND> entity;
    private final Class<READ> responseType;

    public static <SEND, READ> RestRequest.Builder<SEND, READ> builder(){
        return new Builder<>();
    }
    public static <SEND, READ> RestRequest.Builder<SEND,READ> builder(RestRequest<SEND, READ> r){
        return new Builder<>(r);
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

    protected RestRequest(Method method, Location location, Headers headers, Options options, Entity<SEND> entity, Class<READ> responseType) {
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

    public Entity<SEND> getEntity() {
        return entity;
    }

    public Class<READ> getResponseType() {
        return responseType;
    }


    public static class Builder<SEND, READ> {
        private Method method = Method.GET;
        private Location location = Location.Root;
        private Headers headers = Headers.None;
        private Options options = Options.None;
        private Entity<SEND> entity = Entity.None();
        private Class<READ> responseType;

        public Builder(){}
        public Builder(RestRequest<SEND,READ> r){
            this.method = r.method;
            this.location = r.location;
            this.headers = r.headers;
            this.options = r.options;
            this.entity = r.entity;
            this.responseType = r.responseType;
        }

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
        public Builder<SEND, READ> entity(Entity<SEND> entity) {
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
