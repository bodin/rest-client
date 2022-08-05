plugins {
    groovy // spock
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    implementation("com.sun.xml.bind:jaxb-impl:4.0.0")



    // Use the latest Groovy version for Spock testing
    testImplementation("org.codehaus.groovy:groovy:3.0.11")
    testImplementation("org.spockframework:spock-core:2.1-groovy-3.0")
    testImplementation("junit:junit:4.13.2")
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