package io.cobalt

import java.nio.charset.Charset
import java.security.MessageDigest

fun utf8(): Charset = Charset.forName("UTF-8")

fun Long.toHexString(): String = java.lang.Long.toHexString(this)
fun Long.toByteArrayOfHexString(): ByteArray = this.toHexString().toByteArray(utf8())

fun ByteArray.utf8String(): String = String(this, utf8())
fun ByteArray.asHexString(): String = this.fold("") { str, it -> str + "%02x".format(it) }
fun ByteArray.sha256(): ByteArray = MessageDigest.getInstance("SHA-256").digest(this)
