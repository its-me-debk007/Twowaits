package com.example.twowaits

import  android.util.Log
import okhttp3.*
import okio.ByteString
import  java.util.concurrent.TimeUnit

object WebSocketManager {
    private val TAG = WebSocketManager::class.java.simpleName
    private const val maxNoOfRetries = 5  // Maximum number of reconnections
    private const val reconnectionInterval = 5000  // Reconnection interval, milliseconds
    private lateinit var client: OkHttpClient
    private lateinit var request: Request
    private lateinit var messageListener: MessageListener
    private lateinit var mWebSocket: WebSocket
    private var isConnect = false
    private var connectNum = 0
    fun init(url: String, _messageListener: MessageListener) {
        client = OkHttpClient.Builder()
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
        request = Request.Builder().url(url).build()
        messageListener = _messageListener
    }

    fun connect() {
        if (isConnect()) {
            Log.d("VVVV", "web socket connected")
            return
        }
        client.newWebSocket(request, createListener())
    }

    fun reconnect() {
        if (connectNum <= maxNoOfRetries) {
            try {
                Thread.sleep(reconnectionInterval.toLong())
                connect()
                connectNum++
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        } else {
            Log.d("VVVV", "reconnect over $maxNoOfRetries,please check url or network")
        }
    }

    fun isConnect(): Boolean {
        return isConnect
    }

    fun sendMessage(text: String): Boolean {
        return if (!isConnect()) false else mWebSocket.send(text)
    }

    fun sendMessage(byteString: ByteString): Boolean {
        return if (!isConnect()) false else mWebSocket.send(byteString)
    }

    fun close() {
        if (isConnect()) {
            mWebSocket.cancel()
            mWebSocket.close(1001, "The client actively closes the connection ")
        }
    }

    private fun createListener(): WebSocketListener {
        return object : WebSocketListener() {
            override fun onOpen(
                webSocket: WebSocket,
                response: Response
            ) {
                super.onOpen(webSocket, response)
                Log.d("VVVV", "open:$response")
                mWebSocket = webSocket
                isConnect = response.code == 101
                if (!isConnect) {
                    reconnect()
                } else {
                    Log.d("VVVV", "connect success.")
                    messageListener.onConnectSuccess()
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                messageListener.onMessage(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                messageListener.onMessage(bytes.base64())
            }

            override fun onClosing(
                webSocket: WebSocket,
                code: Int,
                reason: String
            ) {
                super.onClosing(webSocket, code, reason)
                isConnect = false
                messageListener.onClose()
            }

            override fun onClosed(
                webSocket: WebSocket,
                code: Int,
                reason: String
            ) {
                super.onClosed(webSocket, code, reason)
                isConnect = false
                messageListener.onClose()
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: Response?
            ) {
                super.onFailure(webSocket, t, response)
                if (response != null) {
                    Log.d("VVVV", "connect failed：" + response.message)
                }
                Log.d(
                    "VVVV", "connect failed throwable：" + t.message
                )
                isConnect = false
                messageListener.onConnectFailed()
                reconnect()
            }
        }
    }
}
