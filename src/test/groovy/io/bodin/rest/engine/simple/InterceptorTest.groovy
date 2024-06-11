
package io.bodin.rest.engine.simple

import io.bodin.rest.BaseServerTest
import io.bodin.rest.client.RestClient
import io.bodin.rest.engine.simple.handlers.JSONContentHandler
import io.bodin.rest.engine.simple.handlers.StringContentHandler
import io.bodin.rest.engine.simple.handlers.XMLContentHandler
import io.bodin.rest.model.Entity
import io.bodin.rest.model.Location
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlRootElement

class InterceptorTest extends BaseServerTest {

    def "GET echo headers"() {
        setup:
        def client = RestClient
                .withSimple("base", BASE.toString())
                .header("X-FOO", 'BAR!')                
                .build()

        when:
        def result = client.get(Location.ofPath("echo-headers"))

        then:
        result.status == 200
        result.headers.collapse().containsKey("X-FOO")
        result.headers.collapse().get("X-FOO") == "BAR!"
    }
}
