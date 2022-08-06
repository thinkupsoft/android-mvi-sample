package com.thinkup.services.api

/**
 * Annotate your class to use a specific url
 * if you dont add to your class, it will be use [BuildConfig.API_URL]
 */
@Target(AnnotationTarget.CLASS)
annotation class ApiUrl(
    val url: String,
    val hasRefreshService: Boolean = true,
    val shouldUseMock: Boolean = false
)
