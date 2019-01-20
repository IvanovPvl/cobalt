package io.cobalt

import java.math.BigInteger

data class RunResult(val nonce: Long, val hash: ByteArray)

class ProofOfWork(private val block: Block) {
    private val targetBits = 20 // TODO: use settings
    private val target: BigInteger = BigInteger.ONE shl (256 - this.targetBits)

    fun validate(): Boolean {
        val data = prepare(this.block.nonce)
        val hashInt = BigInteger(1, data.sha256())
        return hashInt < this.target
    }

    fun run(): RunResult {
        return runHelper(0)
    }

    private fun prepare(nonce: Long): ByteArray {
        val timeBytes = this.block.timestamp.epochSecond.toByteArrayOfHexString()
        val nonceBytes = nonce.toByteArrayOfHexString()
        val targetBytes = this.targetBits.toLong().toByteArrayOfHexString()
        return block.prevBlockHash + block.data + timeBytes + targetBytes + nonceBytes
    }

    private tailrec fun runHelper(nonce: Long): RunResult {
        val data = prepare(nonce)
        val newHash = data.sha256()
        val newHashInt = BigInteger(1, newHash)

        if (newHashInt < target || nonce == Long.MAX_VALUE) {
            return RunResult(nonce, newHash)
        }

        return runHelper(nonce + 1)
    }
}