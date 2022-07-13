
package io.bodin.rest

import com.sun.net.httpserver.HttpServer
import io.bodin.rest.client.RestClient
import io.bodin.rest.engine.simple.SimpleRestEngine
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
        server.createContext("/") { http ->
            http.responseHeaders.add(Headers.CONTENT_TYPE, Entity.TEXT_PLAIN)
            http.sendResponseHeaders(200, 0)
            http.responseBody.withWriter {it << "Hello World"}
        }
        server.createContext("/json") { http ->
            http.responseHeaders.add(Headers.CONTENT_TYPE, Entity.APPLICATION_JSON)
            http.sendResponseHeaders(200, 0)
            http.responseBody.withWriter {it << "{'msg':'Hello World'}"}
        }
        server.start()
    }
    def cleanupSpec(){
        server.stop(0)
    }
}
