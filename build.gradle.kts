plugins {
	java
	id("org.springframework.boot") version "3.4.4" apply false
	id("io.spring.dependency-management") version "1.1.7"
	id("com.diffplug.spotless") version "7.0.3"
}

group = "dev.johnlkramer"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}



allprojects {
	group = "dev.johnlkramer.gotjokes"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
		mavenLocal()
		maven { url = uri("https://repo.spring.io/release") }
	}

	apply(plugin = "com.diffplug.spotless")
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

subprojects {
	apply(plugin="java")
	apply(plugin="io.spring.dependency-management")


	spotless {
		java {
			importOrder()
			formatAnnotations()
			removeUnusedImports()
			palantirJavaFormat()
		}
	}

	dependencyManagement {
		imports {
			mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.4")
			mavenBom("com.fasterxml.jackson:jackson-bom:2.18.3")
		}
	}

	java {
		toolchain {
			languageVersion = JavaLanguageVersion.of(21)
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}

//dependencies {
//	implementation("org.springframework.boot:spring-boot-starter-web")
//	compileOnly("org.projectlombok:lombok")
//	runtimeOnly("org.postgresql:postgresql")
//	annotationProcessor("org.projectlombok:lombok")
//	testImplementation("org.springframework.boot:spring-boot-starter-test")
//	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//}


