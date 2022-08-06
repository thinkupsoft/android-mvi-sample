package com.thinkup.common.services

class SessionException(e: Throwable? = null) : Exception("Session expired!", e)
class UnavailableException(e: Throwable? = null) : Exception("Service unavailable!", e)