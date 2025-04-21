dependencies {
    implementation(project(":model"))
    implementation(project(":api"))
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-webflux:6.2.6")
    implementation("io.projectreactor.netty:reactor-netty-http:1.2.5")
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-databind")

    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testImplementation("org.mock-server:mockserver-junit-jupiter:5.15.0")
}
