plugins {
    `application`
}

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation("com.microsoft.azure:azure-storage:8.6.5")
}

application {
    mainClass.set("pl.mstrejczek.azurite_table_test.MainApp")
}

