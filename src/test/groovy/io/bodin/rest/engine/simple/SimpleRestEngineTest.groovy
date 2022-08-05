
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

class SimpleRestEngineTest extends BaseServerTest {
    def handlers = [new JSONContentHandler(), new XMLContentHandler(), new StringContentHandler()]

    @XmlAccessorType( XmlAccessType.FIELD )
    @XmlRootElement
    static class Doc {
        String msg;

        Doc() {}

        Doc(String msg) {this.msg = msg}
    }

    def "GET request with text response"() {
        setup:
        def client = new RestClient(new SimpleRestEngine(BASE, handlers))

        when:
        def result = client.get(Location.ofPath("text"))

        then:
        result.status == 200
        result.entity == "Hello World"
    }

    def "GET request with JSON response"() {
        setup:
        def client = new RestClient(new SimpleRestEngine(BASE, handlers))

        when:
        def result = client.get(Location.ofPath("json"), Doc.class)

        then:
        result.status == 200
        result.entity.msg == "Hello World"
    }

    def "GET request with XML response"() {
        setup:
        def client = new RestClient(new SimpleRestEngine(BASE, handlers))

        when:
        def result = client.get(Location.ofPath("xml"), Doc.class)

        then:
        result.status == 200
        result.entity.msg == "Hello World"
    }

    def "POST request with text response"() {
        setup:
        def client = new RestClient(new SimpleRestEngine(BASE, handlers))

        when:
        def result = client.post(Location.ofPath("text"), Entity.ofPlain("Hello TEXT!!!"), String.class)

        then:
        result.status == 200
        result.entity == "Hello TEXT!!!"
    }

    def "POST request with JSON response"() {
        setup:
        def client = new RestClient(new SimpleRestEngine(BASE, handlers))

        when:
        def result = client.post(Location.ofPath("json"), Entity.ofJson(new Doc("Hello JSON!!!")), Doc.class)

        then:
        result.status == 200
        result.entity.msg == "Hello JSON!!!"
    }

    def "POST request with XML response"() {
        setup:
        def client = new RestClient(new SimpleRestEngine(BASE, handlers))

        when:
        def result = client.post(Location.ofPath("xml"), Entity.ofXml(new Doc("Hello XML!!!")), Doc.class)

        then:
        result.status == 200
        result.entity.msg == "Hello XML!!!"
    }
}
