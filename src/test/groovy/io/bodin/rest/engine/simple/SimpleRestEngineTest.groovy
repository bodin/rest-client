
package io.bodin.rest.engine.simple

import io.bodin.rest.Location
import io.bodin.rest.client.RestClient
import spock.lang.Specification
import com.sun.net.httpserver.HttpServer

class SimpleRestEngineTest extends Specification {
    def "test a simple GET request"() {
        setup:
        def client = new RestClient(new SimpleRestEngine(URI.create("http://localhost:8080").toURL()))

        int PORT = 8080
        def server = HttpServer.create(new InetSocketAddress(PORT), /*max backlog*/ 0)
        server.createContext("/") { http ->
            http.responseHeaders.add("Content-type", "text/plain")
            http.sendResponseHeaders(200, 0)
            http.responseBody.withWriter { out ->
                out << "Hello World"
            }
            println "Hit from Host"
        }

        when:
        server.start()
        def result = client.get(Location.withRoot().build())
        server.stop(0)

        then:
        result.status == 200
        result.entity == "Hello World".bytes
    }
}
