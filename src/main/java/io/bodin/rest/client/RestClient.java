package io.bodin.rest.client;

import io.bodin.rest.engine.RestEngine;
import io.bodin.rest.model.*;

import java.io.IOException;

public class RestClient {

    private RestEngine engine;

    public RestClient(RestEngine engine) {
        this.engine = engine;
    }

    public <SEND,READ> RestResponse<READ> execute(RestRequest<SEND,READ> request) throws IOException {
        return this.engine.execute(request);
    }
    public RestResponse<String> get(Location location) throws IOException {
        return this.execute(create(Method.GET, location, Options.None, Headers.None, Entity.None(), String.class));
    }

    public <READ> RestResponse<READ> get(Location location, Class<READ> type) throws IOException {
        return this.execute(create(Method.GET, location, Options.None, Headers.None, Entity.None(), type));
    }

    public <SEND,READ> RestResponse<READ> post(Location location, Entity<SEND> e, Class<READ> type) throws IOException {
        return this.execute(create(Method.POST, location, Options.None, Headers.None, e, type));
    }

    private static <T,S> RestRequest<T,S> create(Method m, Location l, Options o, Headers h, Entity<T> entity, Class<S> type){
        return RestRequest.<T,S>builder()
                .method(m == null ? Method.GET : m)
                .location(l == null ? Location.Root : l)
                .options(o == null ? Options.None : o)
                .headers(h == null ? Headers.None : h)
                .entity(entity == null ? null : entity)
                .responseType(type)
                .build();
    }
}
