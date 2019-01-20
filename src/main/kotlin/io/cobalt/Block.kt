package io.cobalt

import java.io.Serializable
import java.time.Instant
import java.security.MessageDigest

data class Block(val timestamp: Instant,
                 val data: ByteArray,
                 val prevBlockHash: ByteArray,
                 val hash: ByteArray,
                 val nonce: Long) : Serializable {

    override fun toString(): String {
        val timeStr = this.timestamp.toString()
        val hashStr = this.hash.asHexString()
        val prevHashStr = this.prevBlockHash.asHexString()

        return "timestamp: $timeStr\ndata: ${this.data.utf8String()}\nprevBlockHash: $prevHashStr\nhash: $hashStr\n"
    }

    companion object {
        fun new(data: ByteArray, prevBlockHash: ByteArray): Block {
            val timestamp = Instant.now()
            val headers = prevBlockHash + data + timestamp.epochSecond.toByteArrayOfHexString()
            val hash = MessageDigest.getInstance("SHA-256").digest(headers)

            val block = Block(timestamp, data, prevBlockHash, hash, 0)
            val pow = ProofOfWork(block)
            val (newNonce, newHash) = pow.run()
            return block.copy(hash = newHash, nonce = newNonce)
        }
    }
}
