plugins {
    alias(libs.plugins.common.android.library)
    alias(libs.plugins.common.android.hilt)
}

android {
    namespace = "com.groupec.cleanarchitecturesampleapp.common"
}

dependencies {
    // Test
    testImplementation(libs.kotlinx.coroutines.test)
}