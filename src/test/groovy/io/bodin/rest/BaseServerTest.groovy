
package io.bodin.rest

import com.sun.net.httpserver.HttpServer
import io.bodin.rest.model.Entity
import io.bodin.rest.model.Headers
import spock.lang.Shared
import spock.lang.Specification

class BaseServerTest extends Specification {

    @Shared
    protected HttpServer server

    @Shared
    protected URL BASE = URI.create("http://localhost:8080").toURL()

    def setupSpec() {
        server = HttpServer.create(new InetSocketAddress(8080), /*max backlog*/ 0)
        server.createContext("/text") { http ->
            http.responseHeaders.add(Headers.CONTENT_TYPE, Entity.TEXT_PLAIN)
            if(http.requestMethod == 'POST'){
                byte[] request = http.requestBody.readAllBytes()
                http.sendResponseHeaders(200, 0)
                http.responseBody.with{it << request; it.close()}
            } else if(http.requestMethod == 'GET'){
                http.sendResponseHeaders(200, 0)
                http.responseBody.withWriter {it << "Hello World"}
            }else{
                http.sendResponseHeaders(404, 0)
            }
        }
        server.createContext("/json") { http ->
            http.responseHeaders.add(Headers.CONTENT_TYPE, Entity.APPLICATION_JSON)
            if(http.requestMethod == 'POST'){
                byte[] request = http.requestBody.readAllBytes()
                println(new String(request))
                http.sendResponseHeaders(200, 0)
                http.responseBody.with{it << request; it.close()}
            } else if(http.requestMethod == 'GET'){
                http.sendResponseHeaders(200, 0)
                http.responseBody.withWriter {it << "{'msg':'Hello World'}"}
            }else{
                http.sendResponseHeaders(404, 0)
            }
        }
        server.createContext("/xml") { http ->
            http.responseHeaders.add(Headers.CONTENT_TYPE, Entity.APPLICATION_XML)
            if(http.requestMethod == 'POST'){
                byte[] request = http.requestBody.readAllBytes()
                http.sendResponseHeaders(200, 0)
                http.responseBody.with{it << request; it.close()}
            } else if(http.requestMethod == 'GET'){
                http.sendResponseHeaders(200, 0)
                http.responseBody.withWriter {it << "<doc><msg>Hello World</msg></doc>"}
            }else{
                http.sendResponseHeaders(404, 0)
            }
        }
        server.createContext("/echo-headers") { http ->
            http.requestHeaders.entrySet().forEach(h -> {
                h.getValue().forEach(v -> http.responseHeaders.add(h.getKey(), v))
            })
            http.sendResponseHeaders(200, 0)
        }
        server.start()
    }
    def cleanupSpec(){
        server.stop(0)
    }
}
