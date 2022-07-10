package io.bodin.rest.engine;

import io.bodin.rest.RestRequest;
import io.bodin.rest.RestResponse;

import java.io.IOException;

public interface RestEngine {
    <SEND, READ> RestResponse<READ> execute(RestRequest<SEND, READ> request) throws IOException;
}
