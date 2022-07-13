package io.bodin.rest.engine.simple;

import io.bodin.rest.engine.RestEngine;
import io.bodin.rest.engine.simple.handlers.ContentHandler;
import io.bodin.rest.model.Headers;
import io.bodin.rest.model.RestRequest;
import io.bodin.rest.model.RestResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            case GET: return get(request);
        }
        throw new IOException("Unknown method " + request.getMethod());
    }

    private <SEND, READ> RestResponse<READ> get(RestRequest<SEND, READ> r) throws IOException{
        URL url = r.getLocation().toURL(this.base);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try{
            con.setRequestMethod("GET");
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
                con.setRequestProperty(Headers.CONTENT_TYPE, r.getEntity().getContentType());
                con.setDoOutput(true);
                //serialize output
            }
            int status = con.getResponseCode();
            String contentType = con.getHeaderField(Headers.CONTENT_TYPE);

            for(ContentHandler h : this.handlers){
                if(h.isSupported(contentType, r.getResponseType())){
                    READ body = h.parse(con.getInputStream(), r.getResponseType());
                    return RestResponse.of(status, body);
                }
            }

            throw new UnsupportedOperationException("Can't serializable to " + r.getResponseType());

        }finally{
            con.disconnect();
        }
    }
}
