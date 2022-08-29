package io.bodin.rest.model;

import java.io.IOException;

public interface Interceptor {
    <SEND, READ> RestResponse<READ> intercept(RestRequest<SEND, READ> request, InterceptorChain chain) throws IOException;
}
