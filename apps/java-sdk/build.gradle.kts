plugins {
    `java-library`
    `maven-publish`
    signing
}

group = "com.firecrawl"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    api("com.squareup.okhttp3:okhttp:4.12.0")
    api("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    api("com.fasterxml.jackson.core:jackson-annotations:2.17.2")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.17.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Javadoc> {
    options {
        (this as StandardJavadocDocletOptions).apply {
            addStringOption("Xdoclint:none", "-quiet")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "com.firecrawl"
            artifactId = "firecrawl-java"

            pom {
                name.set("Firecrawl Java SDK")
                description.set("Java SDK for the Firecrawl web scraping API")
                url.set("https://github.com/mendableai/firecrawl")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        name.set("Firecrawl")
                        url.set("https://firecrawl.dev")
                    }
                }

                scm {
                    url.set("https://github.com/mendableai/firecrawl")
                    connection.set("scm:git:git://github.com/mendableai/firecrawl.git")
                    developerConnection.set("scm:git:ssh://github.com/mendableai/firecrawl.git")
                }
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = System.getenv("MAVEN_USERNAME") ?: project.findProperty("ossrhUsername") as String? ?: ""
                password = System.getenv("MAVEN_PASSWORD") ?: project.findProperty("ossrhPassword") as String? ?: ""
            }
        }
    }
}

signing {
    val signingKey = System.getenv("GPG_SIGNING_KEY")
    val signingPassword = System.getenv("GPG_SIGNING_PASSWORD")
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
    }
    sign(publishing.publications["mavenJava"])
}

tasks.withType<Sign>().configureEach {
    onlyIf { System.getenv("GPG_SIGNING_KEY") != null }
}
