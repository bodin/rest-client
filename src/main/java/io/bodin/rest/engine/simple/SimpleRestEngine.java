package io.bodin.rest.engine.simple;

import io.bodin.rest.engine.RestEngine;
import io.bodin.rest.engine.simple.handlers.ContentHandler;
import io.bodin.rest.model.Headers;
import io.bodin.rest.model.RestRequest;
import io.bodin.rest.model.RestResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SimpleRestEngine implements RestEngine {
    private URL base;

    private List<ContentHandler> handlers;

    public SimpleRestEngine(URL base) {
        this(base, Collections.emptyList());
    }
    public SimpleRestEngine(URL base, List<ContentHandler> handlers) {
        this.base = base;
        this.handlers = new ArrayList<>(handlers);
    }


    @Override
    public <SEND, READ> RestResponse<READ> execute(RestRequest<SEND, READ> request) throws IOException {
        switch(request.getMethod()){
            case GET: return execute("GET", request);
            case POST: return execute("POST", request);
        }
        throw new IOException("Unknown method " + request.getMethod());
    }

    private <SEND, READ> RestResponse<READ> execute(String method, RestRequest<SEND, READ> r) throws IOException{
        URL url = r.getLocation().toURL(this.base);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try{
            con.setRequestMethod(method);
            if (r.getOptions().getTimeoutConnect() != null) {
                con.setConnectTimeout((int)r.getOptions().getTimeoutConnect().toMillis());
            }
            if (r.getOptions().getTimeoutRead() != null) {
                con.setReadTimeout((int) r.getOptions().getTimeoutRead().toMillis());
            }
            for(Map.Entry<String, String> e : r.getHeaders().collapse().entrySet()){
                con.setRequestProperty(e.getKey(), e.getValue());
            }

            if(r.getEntity().getEntity() != null){
                boolean written = false;
                for(ContentHandler h : this.handlers) {
                    if (h.isSupportedWrite(r.getEntity())) {
                        con.setRequestProperty(Headers.CONTENT_TYPE, r.getEntity().getContentType());
                        con.setDoOutput(true);
                        h.write(con.getOutputStream(), r.getEntity());
                        written = true;
                        break;
                    }
                }
                if(!written) throw new UnsupportedOperationException("Can't serializable as " + r.getEntity().getContentType());
            }

            int status = con.getResponseCode();
            Headers.Builder headers = Headers.builder();
            String contentType = con.getHeaderField(Headers.CONTENT_TYPE);
            con.getHeaderFields().entrySet().forEach(e -> {
                if(e.getKey() != null) {
                    headers.header(e.getKey(), String.join(",", e.getValue()));
                }
            });

            for(ContentHandler h : this.handlers){
                if(h.isSupportedParse(contentType, r.getResponseType())){
                    READ body = h.parse(con.getInputStream(), r.getResponseType());
                    return RestResponse.of(status, headers.build(), body);
                }
            }

            throw new UnsupportedOperationException("Can't serializable to " + r.getResponseType());

        }finally{
            con.disconnect();
        }
    }
}
