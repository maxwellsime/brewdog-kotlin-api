group = "com.punk"
version = "0.0.2"

application {
    mainClass.set("com.punk.ApplicationKt.main")
}

plugins {
    application
    kotlin("jvm") version "1.9.0"
    "version-catalog"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation(libs.bundles.ktor.client)
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.serialization)
    implementation(libs.bundles.koin)
    implementation(libs.kotlin.logging)
    implementation(libs.sl4j.logger)
    testImplementation(libs.bundles.ktor.test)
    testImplementation(libs.kotest)
    testImplementation(libs.mockk)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
