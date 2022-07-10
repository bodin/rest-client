
package io.bodin.rest.engine.simple

import io.bodin.rest.BaseTest
import io.bodin.rest.Location
import io.bodin.rest.client.RestClient

class SimpleRestEngineTest extends BaseTest {
    def "test a simple GET request"() {
        setup:
        def client = new RestClient(new SimpleRestEngine(BASE))

        when:
        def result = client.get(Location.Root)

        then:
        result.status == 200
        result.entity == "Hello World".bytes
    }
}
