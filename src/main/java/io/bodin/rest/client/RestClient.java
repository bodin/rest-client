package io.bodin.rest.client;

import io.bodin.rest.*;
import io.bodin.rest.engine.RestEngine;

import java.io.IOException;

public class RestClient {

    private RestEngine engine;

    public RestClient(RestEngine engine) {
        this.engine = engine;
    }

    public <T,S> RestResponse<S> execute(RestRequest<T,S> request) throws IOException {
        return this.engine.execute(request);
    }
    public RestResponse<byte[]> get(Location location) throws IOException {
        return this.engine.execute(create(Method.GET, location, null, null, null, byte[].class));
    }

    private static <T,S> RestRequest<T,S> create(Method m, Location l, Options o, Headers h, T entity, Class<S> type){
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
