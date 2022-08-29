package io.bodin.rest.client;

import io.bodin.rest.engine.RestEngine;
import io.bodin.rest.model.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestClient {

    private InterceptorChain chain;

    public RestClient(RestEngine engine) {
        this.chain = new InternalInterceptorChain(engine);
    }
    public RestClient(RestEngine engine, List<Interceptor> chain) {
        this.chain = new InternalInterceptorChain(engine, chain);
    }

    public RestClient(RestEngine engine, Interceptor next, Interceptor ... rest) {
        List<Interceptor> incs = new ArrayList<>();
        incs.add(next);
        incs.addAll(Arrays.asList(rest));
        this.chain = new InternalInterceptorChain(engine, incs);
    }

    public <SEND,READ> RestResponse<READ> execute(RestRequest<SEND,READ> request) throws IOException {
        return this.chain.next(request);
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

    private static class InternalInterceptorChain implements InterceptorChain {
        private Interceptor next;
        private InterceptorChain chain;

        public InternalInterceptorChain(RestEngine engine){
            this.next = new InternalInterceptor(engine);
        }

        public InternalInterceptorChain(RestEngine engine, List<Interceptor> rest){
            if(rest == null || rest.isEmpty()){
                this.next = new InternalInterceptor(engine);
            }else{
                this.next = rest.get(0);
                this.chain = new InternalInterceptorChain(engine, rest.subList(1, rest.size()));
            }
        }

        @Override
        public <SEND, READ> RestResponse<READ> next(RestRequest<SEND, READ> request) throws IOException {
            return next.intercept(request, chain);
        }
    }

    private static class InternalInterceptor implements Interceptor {
        private RestEngine engine;
        public InternalInterceptor(RestEngine engine){
            this.engine = engine;
        }
        @Override
        public <SEND, READ> RestResponse<READ> intercept(RestRequest<SEND, READ> request, InterceptorChain chain) throws IOException {
            return this.engine.execute(request);
        }
    }

}
