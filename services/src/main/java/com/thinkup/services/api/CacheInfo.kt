package com.thinkup.services.api

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheInfo(
    /**
     * life time cache online is defined in Seconds
     **/
    val lifeTimeCache: Int = 0,
    /**
     * life time offline is defined in Days
     **/
    val lifeTimeOffline: Int = 0,
    /**
     * life time local database is defined in Seconds
     **/
    val lifeTimeDatabase: Int = 0
)