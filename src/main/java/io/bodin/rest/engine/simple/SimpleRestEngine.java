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

            //con.setRequestProperty("Content-Type", "application/json");

            int status = con.getResponseCode();
            StringBuffer content = new StringBuffer();

            try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }

            @SuppressWarnings("unchecked")
            READ body = (READ)content.toString().getBytes(Charset.defaultCharset());

            return new RestResponse<>(status, body);

        }finally{
            con.disconnect();
        }
    }
}
