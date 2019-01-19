package io.cobalt

import java.nio.charset.Charset

fun utf8(): Charset = Charset.forName("UTF-8")

fun Long.toHexString(): String = java.lang.Long.toHexString(this)
fun Long.toByteArrayOfHexString(): ByteArray = this.toHexString().toByteArray(utf8())

fun String.utf8Bytes(): ByteArray = this.toByteArray(utf8())

fun ByteArray.utf8String(): String = String(this, utf8())
fun ByteArray.asHexString(): String = this.fold("") { str, it -> str + "%02x".format(it) }
