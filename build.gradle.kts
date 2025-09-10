plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.google.devtools.ksp") version "1.9.25-1.0.20"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "com.zunza"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("io.jsonwebtoken:jjwt:0.12.6") // JWT
	implementation("io.github.oshai:kotlin-logging-jvm:7.0.3") // Logging
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client") // oauth2
	implementation("io.github.openfeign.querydsl:querydsl-jpa:6.11") //OpenFeign QueryDSL
	ksp("io.github.openfeign.querydsl:querydsl-ksp-codegen:6.11")
	annotationProcessor("io.github.openfeign.querydsl:querydsl-apt:6.11:jakarta")
	implementation("org.springframework.boot:spring-boot-starter-actuator") // actuator
	implementation("io.micrometer:micrometer-registry-prometheus") // micrometer
//	implementation("com.google.firebase:firebase-admin:9.2.0") // Firebase

	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("io.kotest:kotest-runner-junit5-jvm:5.9.1") // kotest
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
	testImplementation("io.mockk:mockk:1.14.5") // Mockk
	testImplementation("org.springframework.boot:spring-boot-testcontainers:3.5.4") // Test Containers
	testImplementation("org.testcontainers:junit-jupiter:1.21.3")
	testImplementation("org.testcontainers:mysql")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.jar {
	enabled = false
}
