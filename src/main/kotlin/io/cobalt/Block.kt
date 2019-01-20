package io.cobalt

import java.time.Instant
import java.io.Serializable

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

            val block = Block(timestamp, data, prevBlockHash, headers.sha256(), 0)
            val pow = ProofOfWork(block)
            val (newNonce, newHash) = pow.run()
            return block.copy(hash = newHash, nonce = newNonce)
        }

        fun genesis() = new("Genesis Block".utf8Bytes(), arrayOf<Byte>().toByteArray())
    }
}
