plugins {
    `java-library`
}

dependencies {
    implementation("org.springframework:spring-context")
    implementation(project(":data"))
    implementation(project(":model"))
    implementation(project(":api"))
}
