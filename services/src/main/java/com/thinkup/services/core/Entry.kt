package com.thinkup.services.core

import okhttp3.*
import okhttp3.Cache.Companion.varyHeaders
import okhttp3.internal.http.StatusLine
import okhttp3.internal.platform.Platform
import java.io.BufferedReader
import java.io.IOException
import java.util.*
import okio.Buffer
import okio.ByteString.Companion.encodeUtf8

class Entry private constructor() {

    private var url: String = ""
    private var body: String = ""
    private var varyHeaders: Headers? = null
    private var requestMethod: String = ""
    private var protocol: Protocol = Protocol.HTTP_1_1
    private var code: Int = 0
    private var message: String = ""
    private var responseHeaders: Headers? = null
    private var handshake: Handshake? = null
    private var sentRequestMillis: Long = 0L
    private var receivedResponseMillis: Long = 0L

    constructor(response: Response) : this() {
        try {
            this.url = response.request.url.toString()
            this.body = hashBody(response.request)
            this.requestMethod = response.request.method
            this.protocol = response.protocol
            this.code = response.code
            this.message = response.message
            this.responseHeaders = response.headers
            this.handshake = response.handshake
            this.sentRequestMillis = response.sentRequestAtMillis
            this.receivedResponseMillis = response.receivedResponseAtMillis
            this.varyHeaders = response.varyHeaders()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @Throws(IOException::class)
    constructor(source: BufferedReader) : this() {
        try {

            source.use { source ->
                url = source.readLine()
                requestMethod = source.readLine()
                val varyHeadersBuilder = Headers.Builder()
                val varyRequestHeaderLineCount = source.readLine().toInt()
                for (i in 0 until varyRequestHeaderLineCount) {
                    val split = source.readLine().split(": ")
                    varyHeadersBuilder.add(split[0], split[1])
                }
                varyHeaders = varyHeadersBuilder.build()

                val statusLine = StatusLine.parse(source.readLine())
                protocol = statusLine.protocol
                code = statusLine.code
                message = statusLine.message
                val responseHeadersBuilder = Headers.Builder()
                val responseHeaderLineCount = source.readLine().toInt()
                for (i in 0 until responseHeaderLineCount) {
                    val split = source.readLine().split(": ")
                    responseHeadersBuilder.add(split[0], split[1])
                }
                val sendRequestMillisString = responseHeadersBuilder.get(SENT_MILLIS)
                val receivedResponseMillisString = responseHeadersBuilder.get(RECEIVED_MILLIS)
                responseHeadersBuilder.removeAll(SENT_MILLIS)
                responseHeadersBuilder.removeAll(RECEIVED_MILLIS)
                sentRequestMillis = java.lang.Long.parseLong(sendRequestMillisString)
                receivedResponseMillis = java.lang.Long.parseLong(receivedResponseMillisString)
                responseHeaders = responseHeadersBuilder.build()

                if (isHttps()) {
                    val blank = source.readLine()
                    if (blank.isNotEmpty()) {
                        throw IOException("expected \"\" but was \"$blank\"")
                    }
                    val cipherSuiteString = source.readLine()
                    val cipherSuite = CipherSuite.forJavaName(cipherSuiteString)
                    val tlsString = source.readLine()
                    val tlsVersion = if (!tlsString.isNullOrEmpty())
                        TlsVersion.forJavaName(tlsString)
                    else
                        TlsVersion.SSL_3_0
                    handshake = Handshake.get(tlsVersion, cipherSuite, listOf(), listOf())
                } else {
                    handshake = null
                }

                val blank = source.readLine()
                if (blank.isNotEmpty()) {
                    throw IOException("expected \"\" but was \"$blank\"")
                }
                body = source.readLine()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun bodyToString(request: Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }

    fun matches(request: Request, maxTime: Int): Boolean {
        return url == request.url.toString()
                && requestMethod == request.method
                && validateAge(maxTime)
                && hashBody(request) == body
    }

    private fun hashBody(request: Request): String {
        return bodyToString(request).encodeUtf8().md5().hex()
    }

    private fun validateAge(maxTime: Int): Boolean {
        val current = Calendar.getInstance().time.time
        val expiration = getTimeHeader((maxTime * 1000).toLong())
        return current < expiration
    }

    private fun getTimeHeader(addTime: Long): Long {
        return if (sentRequestMillis != 0L)
            sentRequestMillis.plus(addTime)
        else Long.MAX_VALUE
    }

    @Throws(IOException::class)
    fun writeTo(): String {
        val sink = StringBuilder()
        sink.append(url)
        sink.append('\n')
        sink.append(requestMethod)
        sink.append('\n')
        sink.append(varyHeaders?.size?.toLong() ?: 0)
        sink.append('\n')
        run {
            var i = 0
            val size = varyHeaders?.size ?: 0
            while (i < size) {
                sink.append(varyHeaders?.name(i))
                sink.append(": ")
                sink.append(varyHeaders?.value(i))
                sink.append('\n')
                i++
            }
        }

        sink.append(StatusLine(protocol, code, message).toString())
        sink.append('\n')
        sink.append(((responseHeaders?.size ?: 0) + 2).toLong())
        sink.append('\n')
        var i = 0
        val size = responseHeaders?.size ?: 0
        while (i < size) {
            sink.append(responseHeaders?.name(i))
            sink.append(": ")
            sink.append(responseHeaders?.value(i))
            sink.append('\n')
            i++
        }
        sink.append(SENT_MILLIS)
        sink.append(": ")
        sink.append(sentRequestMillis)
        sink.append('\n')
        sink.append(RECEIVED_MILLIS)
        sink.append(": ")
        sink.append(receivedResponseMillis)
        sink.append('\n')

        if (isHttps()) {
            sink.append('\n')
            sink.append(handshake!!.cipherSuite.javaName)
            sink.append('\n')
            sink.append(handshake!!.tlsVersion.javaName)
            sink.append('\n')
        }
        sink.append('\n')
        sink.append(body)


        return sink.toString()
    }

    private fun isHttps(): Boolean {
        return url.startsWith("https://")
    }

    companion object {
        /** Synthetic response header: the local time when the request was sent.  */
        private val SENT_MILLIS = Platform.get().getPrefix() + "-Sent-Millis"

        /** Synthetic response header: the local time when the response was received.  */
        private val RECEIVED_MILLIS = Platform.get().getPrefix() + "-Received-Millis"
    }
}