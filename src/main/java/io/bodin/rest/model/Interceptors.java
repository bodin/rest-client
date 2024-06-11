package io.bodin.rest.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;

public class Interceptors {
    public static Interceptor headers(Headers headers){
        return new HeaderInterceptor(headers);
    }

    public static Interceptor header(String name, String value){
        return new HeaderInterceptor(Headers.withHeader(name, value).build());
    }

    public static Interceptor log(String id){
        return new LogInterceptor(id, "audit.rest." + id);
    }

    public static Interceptor log(String id, String loggerName){
        return new LogInterceptor(id, loggerName);
    }

    public static class LogInterceptor implements Interceptor {
        private String id;
        private Logger log;

        LogInterceptor(String id, String logName){
            this.id = id;
            this.log = LoggerFactory.getLogger(logName);
        }
        @Override
        public <SEND, READ> RestResponse<READ> intercept(RestRequest<SEND, READ> request, InterceptorChain chain) throws IOException {
            long start = System.currentTimeMillis();
            var response =  chain.next(request);
            long total = System.currentTimeMillis() - start;
            MDC.put("DURATION", String.valueOf(total));
            MDC.put("REST_METHOD", String.valueOf(System.currentTimeMillis() - start));
            MDC.put("REST_URL", String.valueOf(System.currentTimeMillis() - start));
            MDC.put("REST_RESPONSE_CODE", String.valueOf(System.currentTimeMillis() - start));
            log.info("[{}] {} {} {} @ {}",id, response.getStatus(), request.getMethod(), request.getLocation().toString(), total);
            MDC.remove("DURATION");
            MDC.remove("REST_METHOD");
            MDC.remove("REST_URL");
            MDC.remove("REST_RESPONSE_CODE");
            return response;
        }
    }
    public static class HeaderInterceptor implements Interceptor {
        private Headers headers;

        public HeaderInterceptor(Headers headers) {
            this.headers = headers;
        }

        @Override
        public <SEND, READ> RestResponse<READ> intercept(RestRequest<SEND, READ> request, InterceptorChain chain) throws IOException {
            Headers.Builder newHeaders = Headers.withHeaders(request.getHeaders());
            headers.forEach((h) -> newHeaders.header(h.getName(), h.getValue()));

            RestRequest<SEND, READ> newRequest = RestRequest
                    .builder(request)
                    .headers(newHeaders.build())
                    .build();

            return chain.next(newRequest);
        }
    }
}
