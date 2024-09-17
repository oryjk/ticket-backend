rootProject.name = "ticket-backend"

pluginManagement {
    repositories {

        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
        }
        maven {
            url = uri("https://jfrog.betalpha.com/artifactory/plugins-release/")
            credentials {
                username = "betalpha1"
                password = "Beifa888"
            }
        }

        mavenCentral()
        mavenLocal()
    }
}
