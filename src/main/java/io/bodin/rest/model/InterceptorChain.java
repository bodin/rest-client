package io.bodin.rest.model;

import java.io.IOException;

public interface InterceptorChain {
    <SEND, READ> RestResponse<READ> next(RestRequest<SEND, READ> request) throws IOException;
}
