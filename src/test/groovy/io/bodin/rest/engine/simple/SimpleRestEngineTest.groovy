
package io.bodin.rest.engine.simple

import io.bodin.rest.BaseServerTest
import io.bodin.rest.model.Location
import io.bodin.rest.client.RestClient

class SimpleRestEngineTest extends BaseServerTest {
    def "test a simple GET request"() {
        setup:
        def client = new RestClient(new SimpleRestEngine(BASE))

        when:
        def result = client.get(Location.Root)

        then:
        result.status == 200
        result.entity == "Hello World"
    }
}
