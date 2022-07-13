
package io.bodin.rest.engine.simple

import io.bodin.rest.BaseServerTest
import io.bodin.rest.client.RestClient
import io.bodin.rest.engine.simple.handlers.JsonContentHandler
import io.bodin.rest.engine.simple.handlers.StringContentHandler
import io.bodin.rest.model.Location

class SimpleRestEngineTest extends BaseServerTest {
    def "test a simple GET request with text response"() {
        setup:
        def handlers = [new JsonContentHandler(), new StringContentHandler()]
        def client = new RestClient(new SimpleRestEngine(BASE, handlers))

        when:
        def result = client.get(Location.Root)

        then:
        result.status == 200
        result.entity == "Hello World"
    }

    def "test a simple GET request with JSON response"() {
        setup:
        def handlers = [new JsonContentHandler(), new StringContentHandler()]
        def client = new RestClient(new SimpleRestEngine(BASE, handlers))

        when:
        def result = client.get(Location.ofPath("json"), HashMap.class)

        then:
        result.status == 200
        result.entity.containsKey("msg")
        result.entity.get("msg") == "Hello World"
        result.entity.size() == 1
    }
}
