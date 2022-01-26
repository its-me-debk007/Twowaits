package com.example.twowaits

interface MessageListener {
    fun  onConnectSuccess ()
    fun  onConnectFailed ()
    fun  onClose () // close
    fun onMessage(text: String?)
}
