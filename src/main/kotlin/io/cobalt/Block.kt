package io.cobalt

import java.time.Instant
import java.security.MessageDigest

class Block(private val data: ByteArray, private val prevBlockHash: ByteArray) {
    private val timestamp: Instant = Instant.now()
    val hash: ByteArray

    init {
        val headers = prevBlockHash + data + timestamp.epochSecond.toByteArrayOfHexString()
        hash = MessageDigest.getInstance("SHA-256").digest(headers)
    }

    override fun toString(): String {
        val timeStr = this.timestamp.toString()
        val hashStr = this.hash.asHexString()
        val prevHashStr = this.prevBlockHash.asHexString()

        return "timestamp: $timeStr\ndata: ${this.data.utf8String()}\nprevBlockHash: $prevHashStr\nhash: $hashStr\n"
    }
}
