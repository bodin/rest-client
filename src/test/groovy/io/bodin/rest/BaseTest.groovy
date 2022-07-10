
package io.bodin.rest

import com.sun.net.httpserver.HttpServer
import io.bodin.rest.client.RestClient
import io.bodin.rest.engine.simple.SimpleRestEngine
import spock.lang.Shared
import spock.lang.Specification

class BaseTest extends Specification {

    @Shared
    protected HttpServer server

    @Shared
    protected URL BASE = URI.create("http://localhost:8080").toURL()

    def setupSpec() {
        server = HttpServer.create(new InetSocketAddress(8080), /*max backlog*/ 0)
        server.createContext("/") { http ->
            http.responseHeaders.add("Content-type", "text/plain")
            http.sendResponseHeaders(200, 0)
            http.responseBody.withWriter {it << "Hello World"}
        }
        server.start()
    }
    def cleanupSpec(){
        server.stop(0)
    }

}
