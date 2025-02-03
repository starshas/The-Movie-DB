plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.android.library") version "8.1.4" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.7" apply false
}

subprojects {
    plugins.apply("org.jlleitschuh.gradle.ktlint")
    plugins.withId("org.jlleitschuh.gradle.ktlint") {
        tasks.register("ktlintVerify") {
            dependsOn("ktlintCheck")
        }
        configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
            verbose.set(true)
            android.set(true)
            enableExperimentalRules.set(true)
        }
    }

    plugins.apply("io.gitlab.arturbosch.detekt")
    plugins.withId("io.gitlab.arturbosch.detekt") {
        tasks.register("detektVerify") {
            dependsOn("detekt")
        }
    }
}

tasks.register("verifyAll") {
    dependsOn(
        subprojects.flatMap { subproject ->
            listOfNotNull(
                subproject.tasks.findByName("ktlintCheck"),
                subproject.tasks.findByName("detekt"),
            )
        },
    )
}
