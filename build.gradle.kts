plugins {
    groovy // spock
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    // Use the latest Groovy version for Spock testing
    testImplementation("org.codehaus.groovy:groovy:3.0.9")

    // Use the awesome Spock testing and specification framework even with Java
    testImplementation("org.spockframework:spock-core:2.0-groovy-3.0")
    testImplementation("junit:junit:4.13.2")

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    //api("org.apache.commons:commons-math3:3.6.1")

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    //implementation("com.google.guava:guava:30.1.1-jre")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
tasks {
    withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:unchecked")
    }
}