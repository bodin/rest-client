package io.bodin.rest.engine.simple;

import io.bodin.rest.RestRequest;
import io.bodin.rest.RestResponse;
import io.bodin.rest.engine.RestEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

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

            //need to collect them into comma separated
            //for(Header h : r.getHeaders()) {
                //con.setRequestProperty(h.getName(), h.getValue());
            //}

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
                return new RestResponse<>(status, body);
            }else if(r.getResponseType() == byte[].class){
                @SuppressWarnings("unchecked")
                READ body = (READ)content.toString().getBytes(Charset.defaultCharset());
                return new RestResponse<>(status, body);
            }else{
                throw new UnsupportedOperationException("Can't serializable to " + r.getResponseType());
            }

        }finally{
            con.disconnect();
        }
    }
}
