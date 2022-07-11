package io.bodin.rest.engine.simple;

import io.bodin.rest.engine.RestEngine;
import io.bodin.rest.model.RestRequest;
import io.bodin.rest.model.RestResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

public class SimpleRestEngine implements RestEngine {
    private URL base;

    public SimpleRestEngine(URL base) {
        this.base = base;
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
            con.setRequestProperty("Content Type", r.getEntity().getContentType());
            if(r.getEntity().getEntity() != null){
                //serialize output
            }
            int status = con.getResponseCode();
            StringBuffer content = new StringBuffer();

            try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }

            if(r.getResponseType() == String.class){
                @SuppressWarnings("unchecked")
                READ body = (READ)content.toString();
                return RestResponse.of(status, body);
            }else if(r.getResponseType() == byte[].class){
                @SuppressWarnings("unchecked")
                READ body = (READ)content.toString().getBytes(Charset.defaultCharset());
                return RestResponse.of(status, body);
            }else{
                throw new UnsupportedOperationException("Can't serializable to " + r.getResponseType());
            }

        }finally{
            con.disconnect();
        }
    }
}
